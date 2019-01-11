package canary.cuplet.scene_main.entity;

import canary.base.collisionbox.box.AxisAlignedBoundingBox;
import canary.base.collisionbox.collider.BoxCollider;
import canary.base.collisionbox.response.CollisionResponse;
import canary.cuplet.basicobject.ComponentRenderable;
import canary.cuplet.scene_main.GobletWorld;
import canary.cuplet.scene_main.component.ComponentMotion;
import canary.cuplet.scene_main.entity.base.EntitySolid;
import canary.cuplet.scene_main.tile.TileMap;

import canary.bstone.transform.Transform3;
import org.joml.Vector2f;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityThrowable extends EntitySolid
{
	public static float GRAVITY = 0.02F;

	float dx;
	float dy;
	float dz;

	protected float restitution;

	private boolean falling;
	private float fallMotion;
	private float fallOffset;

	public EntityThrowable(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, ComponentRenderable renderable, float dx, float dy, float dz)
	{
		super(world, transform, boundingBox, renderable);

		this.dx = dx;
		this.dy = dy;
		this.dz = dz;

		this.fallMotion = dz;
		this.fallOffset = 0;
		this.falling = this.fallMotion > 0;
		this.restitution = 0.6F;

		if (!this.falling)
		{
			this.onFallToGround();
		}
	}

	@Override
	protected void onEntitySetup()
	{
		super.onEntitySetup();

		ComponentMotion componentMotion = this.addComponent(new ComponentMotion());

		componentMotion.motion.set(this.dx, this.dy);
		componentMotion.setFriction(0.05F);
		componentMotion.setOnGround(!this.falling);
	}

	@Override
	protected void onLateUpdate()
	{
		super.onLateUpdate();

		if (this.falling)
		{
			this.fallMotion -= GRAVITY;
			this.fallOffset += this.fallMotion;

			if (this.fallOffset < 0)
			{
				this.fallOffset = 0;
				this.falling = false;
				this.onFallToGround();
				this.getComponent(ComponentMotion.class).setOnGround(true);
			}
			this.getRenderable().getRenderModel().transformation().identity().scale(1 + this.fallOffset / 2F, 1 + this.fallOffset / 2F, 0);
		}
	}

	public void onFallToGround()
	{
	}

	@Override
	public void onPreCollisionUpdate()
	{

	}

	@Override
	public boolean onCollision(CollisionResponse collision)
	{
		if (this.canBounceOff(collision.getCollider()))
		{
			this.move(collision.getDelta().x(), collision.getDelta().y());

			Vector2f motion = this.getComponent(ComponentMotion.class).motion;
			float f = motion.dot(collision.getNormal()) * 2;
			motion.sub(collision.getNormal().x() * f, collision.getNormal().y() * f);
			motion.mul(this.restitution);

			this.onBounce(collision);
		}
		else
		{
			Vector2f motion = this.getComponent(ComponentMotion.class).motion;
			motion.add(collision.getDelta().x() / 100F, collision.getDelta().y() / 100F);
		}

		return false;
	}

	public void onBounce(CollisionResponse collision)
	{

	}

	@Override
	public void onPostCollisionUpdate()
	{

	}

	@Override
	public boolean canCollideWith(BoxCollider collider)
	{
		return collider instanceof TileMap || (collider instanceof EntityThrowable && this.falling == ((EntityThrowable) collider).falling);
	}

	public boolean canBounceOff(BoxCollider collider)
	{
		return collider instanceof TileMap;
	}

	@Override
	public boolean isSolid()
	{
		return !this.falling;
	}

	@Override
	public boolean isColliderActive()
	{
		return true;
	}

	public boolean isFalling()
	{
		return this.falling;
	}
}
