package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityMotion extends EntitySolid
{
	protected Vector2f motion = new Vector2f();
	protected float friction = 0;

	public EntityMotion(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, ComponentRenderable renderable)
	{
		super(world, transform, boundingBox, renderable);
	}

	@Override
	public void onLivingLateUpdate()
	{
		super.onLivingLateUpdate();

		this.onMotionUpdate();
		this.onPositionUpdate(this.motion);
	}

	public void onMotionUpdate()
	{
		this.motion.mul(1 - this.friction);
	}

	public void onPositionUpdate(Vector2fc offset)
	{
		this.transform.translate(offset.x(), offset.y(), 0);

		//TODO: this is redundant!
		if (this.boundingBox != null)
		{
			this.boundingBox.setCenter(this.transform.position3().x(), this.transform.position3().y());
		}
	}

	public void move(float dx, float dy)
	{
		this.transform.translate(dx, dy, 0);

		if (this.boundingBox != null)
		{
			this.boundingBox.offset(dx, dy);
		}
	}

	public final Vector2f getMotion()
	{
		return this.motion;
	}

	public float getFriction()
	{
		return this.friction;
	}
}
