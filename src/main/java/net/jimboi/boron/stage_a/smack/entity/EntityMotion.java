package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;

import org.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 8/7/17.
 */
public abstract class EntityMotion extends SmackEntity
{
	protected Vector2f motion = new Vector2f();
	protected float friction = 0;

	public EntityMotion(SmackWorld world, Transform3 transform, float size, ComponentRenderable renderable)
	{
		super(world, transform, size, renderable);
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

	public void move(float x, float y)
	{
		this.transform.translate(x, y, 0);

		if (this.boundingBox != null)
		{
			this.boundingBox.offset(x, y);
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
