package canary.cuplet.scene_main.component;

import canary.cuplet.scene_main.entity.base.EntityBase;

import canary.bstone.entity.Component;
import canary.bstone.transform.Transform3;
import canary.bstone.transform.Transform3c;
import org.joml.Vector2f;

/**
 * Created by Andy on 8/13/17.
 */
public class ComponentMotion implements Component
{
	public final Vector2f motion = new Vector2f();
	protected float friction = 0;
	protected boolean onGround = true;

	public ComponentMotion setMotion(float dx, float dy)
	{
		this.motion.x = dx;
		this.motion.y = dy;
		return this;
	}

	public ComponentMotion setFriction(float friction)
	{
		this.friction = friction;
		return this;
	}

	public void applyMotion(Transform3 transform)
	{
		transform.translate(this.motion.x(), this.motion.y(), 0);
	}

	public void updateMotion()
	{
		if (this.onGround)
		{
			this.motion.mul(1F - this.friction);
		}
	}

	public void addMotionTowards(Transform3c from, Transform3c to, float magnitude)
	{
		Vector2f vec = new Vector2f(to.posX() - from.posX(), to.posY() - from.posY()).normalize().mul(magnitude);
		this.addMotion(vec.x(), vec.y());
	}

	public void addMotion(float dx, float dy)
	{
		this.motion.x += dx;
		this.motion.y += dy;
	}

	public void setOnGround(boolean onGround)
	{
		this.onGround = onGround;
	}

	public Vector2f getMotion()
	{
		return this.motion;
	}

	public float getFriction()
	{
		return this.friction;
	}

	public static void onLivingLateUpdate(EntityBase entity)
	{
		ComponentMotion componentMotion = entity.getComponent(ComponentMotion.class);
		componentMotion.updateMotion();
		componentMotion.applyMotion(entity.getTransform());

		ComponentBounding componentBounding = entity.getComponent(ComponentBounding.class);
		if (componentBounding != null)
		{
			componentBounding.getBoundingBox().setCenter(entity.getTransform().posX(), entity.getTransform().posY());
		}
	}

	public static void move(EntityBase entity, float dx, float dy)
	{
		entity.getTransform().translate(dx, dy, 0);

		ComponentBounding componentBounding = entity.getComponent(ComponentBounding.class);
		if (componentBounding != null)
		{
			componentBounding.getBoundingBox().offset(dx, dy);
		}
	}
}
