package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.Room;
import net.jimboi.boron.stage_a.goblet.tick.TickCounter;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/13/17.
 */
public class EntityArrow extends EntityMotion implements ActiveBoxCollider
{
	protected final TickCounter ageTicks = new TickCounter(200);

	public EntityArrow(GobletWorld world, Transform3 transform, float dx, float dy)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.1F),
				world.createRenderable2D(transform, '!', 0xFFFF00));

		this.getRenderable().getRenderModel().transformation().rotationZ((float) Math.atan2(dy, dx) + Transform3.HALF_PI).translate(0.25F, 0, 0);

		this.componentMotion.setMotion(dx, dy);
		this.componentMotion.setOnGround(false);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		this.ageTicks.tick();
		if (this.ageTicks.isComplete())
		{
			this.setDead();
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
		this.componentMotion.setMotion(0, 0);

		return true;
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

	@Override
	public boolean isSolid()
	{
		return false;
	}
}