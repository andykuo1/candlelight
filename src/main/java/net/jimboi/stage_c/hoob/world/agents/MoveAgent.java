package net.jimboi.stage_c.hoob.world.agents;

import net.jimboi.stage_c.hoob.EntityComponentBoundingCollider;
import net.jimboi.stage_c.hoob.world.World;

import org.bstone.transform.Transform;
import org.joml.Vector2f;
import org.zilar.bounding.BoundingCollider;
import org.zilar.bounding.IntersectionData;
import org.zilar.entity.Entity;

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
		super(world, 0.4F);
	}

	private static final Vector2f _VEC = new Vector2f();

	@Override
	public void onUpdate(double delta)
	{
		Entity entity = this.world.scene.getEntityManager().getEntityByComponent(this);
		BoundingCollider collider = entity.getComponent(EntityComponentBoundingCollider.class).getBounding();

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

			//Smooth sliding
			Vector2f dv = BoundingCollider.checkDeltaWithSlideCollision(collider, new Vector2f(dx, dy));
			IntersectionData intersection = collider.checkIntersection();
			if (intersection != null)
			{
				dv.sub(intersection.delta);
			}
			this.pos.x += dv.x();
			this.pos.y += dv.y();
		}

		collider.update(this.pos.x, this.pos.y);
	}

	public abstract float getMoveSpeed();
}
