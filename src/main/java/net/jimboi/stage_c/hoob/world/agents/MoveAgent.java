package net.jimboi.stage_c.hoob.world.agents;

import net.jimboi.stage_c.hoob.world.World;

import org.joml.Vector2f;
import org.qsilver.transform.Transform;

/**
 * Created by Andy on 7/13/17.
 */
public abstract class MoveAgent extends WorldAgent
{
	public float heading;
	public float targetHeading;

	public final Vector2f target = new Vector2f();

	public MoveAgent(World world)
	{
		super(world);
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
			float dr = this.targetHeading - this.heading;
			if (dr > Transform.PI) this.heading += Transform.PI2;
			else if (dr < -Transform.PI) this.heading -= Transform.PI2;
			this.heading += (this.targetHeading - this.heading) * delta * 12;

			float dx = (float) Math.cos(this.heading) * speed;
			float dy = (float) Math.sin(this.heading) * speed;
			this.pos.x += dx;
			this.pos.y += dy;
		}
	}

	public abstract float getMoveSpeed();
}
