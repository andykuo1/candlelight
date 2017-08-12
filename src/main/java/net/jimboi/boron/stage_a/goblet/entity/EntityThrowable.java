package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.Room;

import org.bstone.transform.Transform3;
import org.joml.Vector2fc;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityThrowable extends EntityMotion implements ActiveBoxCollider
{
	public static float GRAVITY = 0.02F;

	protected float restitution;

	private boolean falling;
	private float fallMotion;
	private float fallOffset;

	public EntityThrowable(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, ComponentRenderable renderable, float dx, float dy, float dz)
	{
		super(world, transform, boundingBox, renderable);

		this.motion.set(dx, dy);
		this.fallMotion = dz;
		this.fallOffset = 0;
		this.falling = this.fallMotion > 0;
		this.friction = 0.1F;
		this.restitution = 0.6F;
	}

	@Override
	public void onMotionUpdate()
	{
		if (this.falling)
		{
			this.fallMotion -= GRAVITY;
			this.fallOffset += this.fallMotion;

			if (this.fallOffset < 0)
			{
				this.fallOffset = 0;
				this.falling = false;
				this.onFallToGround();
			}
		}
		else
		{
			super.onMotionUpdate();
		}
	}

	public void onFallToGround()
	{
	}

	@Override
	public void onPositionUpdate(Vector2fc offset)
	{
		super.onPositionUpdate(offset);

		if (this.falling)
		{
			this.getRenderable().getRenderModel().transformation().identity().scale(1 + this.fallOffset / 2F, 1 + this.fallOffset / 2F, 0);
		}
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

			float f = this.motion.dot(collision.getNormal()) * 2;
			this.motion.sub(collision.getNormal().x() * f, collision.getNormal().y() * f);
			this.motion.mul(this.restitution);

			this.onBounce(collision);
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
		return collider instanceof Room;
	}

	public boolean canBounceOff(BoxCollider collider)
	{
		return collider instanceof Room;
	}

	@Override
	public boolean isSolid()
	{
		return !this.falling;
	}

	public boolean isFalling()
	{
		return this.falling;
	}
}
