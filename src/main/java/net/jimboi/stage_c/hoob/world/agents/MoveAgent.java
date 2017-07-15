package net.jimboi.stage_c.hoob.world.agents;

import net.jimboi.stage_c.hoob.bounding.Collider;
import net.jimboi.stage_c.hoob.world.World;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/13/17.
 */
public abstract class MoveAgent extends WorldAgent
{
	public float heading;
	public float targetHeading;

	public Collider collider;

	public final Vector2f target = new Vector2f();

	public MoveAgent(World world, Collider collider)
	{
		super(world);

		this.collider = collider;
	}

	private static final Vector2f _VEC = new Vector2f();

	@Override
	public void onUpdate(double delta)
	{
		if (this.pos.distanceSquared(this.target) > 0.1F)
		{
			float speed = this.getMoveSpeed() * (float) delta;

			Vector2f vec = new Vector2f(this.target).sub(this.pos);
			this.targetHeading = (float) Math.atan2(vec.y(), vec.x());

			//Smooth rotate
			/*float dr = this.targetHeading - this.heading;
			if (dr > Transform.PI) this.heading += Transform.PI2;
			else if (dr < -Transform.PI) this.heading -= Transform.PI2;
			this.heading += (this.targetHeading - this.heading) * delta * 12;*/
			this.heading = this.targetHeading;

			float dx = (float) Math.cos(this.heading) * speed;
			float dy = (float) Math.sin(this.heading) * speed;

			Vector2fc dv = this.collider.checkCollision(new Vector2f(dx, dy));
			this.pos.x += dv.x();
			this.pos.y += dv.y();
		}

		this.collider.update(this.pos.x, this.pos.y);
	}

	public abstract float getMoveSpeed();

	public Collider getCollider()
	{
		return this.collider;
	}
}
