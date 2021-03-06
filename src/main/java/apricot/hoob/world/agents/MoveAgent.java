package apricot.hoob.world.agents;

import apricot.base.collision.DynamicCollider;
import apricot.base.entity.Entity;
import apricot.hoob.EntityComponentBoundingCollider;
import apricot.hoob.world.World;

import apricot.bstone.transform.Transform;
import org.joml.Vector2f;

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

	@Override
	public void onUpdate()
	{
		final double delta = 0.02F;
		Entity entity = this.world.scene.getEntityManager().getEntityByComponent(this);
		DynamicCollider collider = entity.getComponent(EntityComponentBoundingCollider.class).getCollider();

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
			collider.move(dx, dy);
			collider.update(this.pos.x(), this.pos.y(), this.pos);
		}
	}

	public abstract float getMoveSpeed();
}
