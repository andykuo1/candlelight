package boron.stage_a.goblet.entity;

import boron.stage_a.base.basicobject.ComponentRenderable;
import boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import boron.stage_a.base.collisionbox.collider.BoxCollider;
import boron.stage_a.base.collisionbox.response.CollisionResponse;
import boron.stage_a.goblet.GobletWorld;
import boron.stage_a.goblet.component.ComponentMotion;
import boron.stage_a.goblet.entity.base.EntitySolid;
import boron.stage_a.goblet.tile.TileMap;

import boron.bstone.entity.EntityManager;
import boron.bstone.transform.Transform3;
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
	public void onEntityCreate(EntityManager entityManager)
	{
		super.onEntityCreate(entityManager);

		ComponentMotion componentMotion = this.addComponent(new ComponentMotion());

		componentMotion.motion.set(this.dx, this.dy);
		componentMotion.setFriction(0.05F);
		componentMotion.setOnGround(!this.falling);
	}

	@Override
	public void onLivingLateUpdate()
	{
		super.onLivingLateUpdate();

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
