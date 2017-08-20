package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.Room;
import net.jimboi.boron.stage_a.goblet.tick.TickCounter;

import org.bstone.transform.Transform3;
import org.joml.Vector3f;

/**
 * Created by Andy on 8/13/17.
 */
public class EntitySkeleton extends EntityAlertable implements ActiveBoxCollider
{
	protected final TickCounter shootTicks = new TickCounter(100);

	public EntitySkeleton(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.5F),
				world.createRenderable2D(transform, 'S', 0xAAAAAA));

		this.maxHealth = 10;
		this.alertSpeed = 0.04F;
		this.componentMotion.setFriction(0.3F);
	}

	@Override
	public void onAlertTick()
	{
		super.onAlertTick();

		this.shootTicks.tick();
		if (this.shootTicks.isComplete())
		{
			this.shootTicks.reset();
			Vector3f vec = this.transform.position3().sub(this.target.getTransform().position3(), new Vector3f()).normalize().mul(0.5F).negate();
			this.world.spawnEntity(new EntityArrow(this.world, this.world.createTransform(this.transform), vec.x(), vec.y()));
		}
	}

	@Override
	public void onPreCollisionUpdate()
	{

	}

	@Override
	public boolean onCollision(CollisionResponse collision)
	{
		this.move(collision.getDelta().x(), collision.getDelta().y());

		return false;
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
}
