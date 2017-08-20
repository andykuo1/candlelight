package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionSolver;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.Room;
import net.jimboi.boron.stage_a.goblet.tick.TickCounter;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/13/17.
 */
public class EntityAlertable extends EntityHurtable
{
	protected EntityBase target;

	protected final TickCounter alertTicks = new TickCounter(100);

	protected float alertSpeed = 0.03F;

	public EntityAlertable(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, ComponentRenderable renderable)
	{
		super(world, transform, boundingBox, renderable);
	}

	public void onIdleTick()
	{

	}

	public void onIdle()
	{

	}

	public void onAlert(EntityBase entity)
	{

	}

	public void onAlertTick()
	{

	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		EntityPlayer entityPlayer = this.getWorld().getPlayer();
		Room room = this.world.getRoom(this.transform.posX(), this.transform.posY());
		float dx = (this.transform.posX() - entityPlayer.getTransform().posX());
		float dy = (this.transform.posY() - entityPlayer.getTransform().posY());
		CollisionResponse response = CollisionSolver.solve(this.transform.posX(), this.transform.posY(), dx, dy, 0.3F, 0.3F, room.getBoundingBox());
		if (response == null)
		{
			this.target = entityPlayer;
			this.alertTicks.reset();
			this.onAlert(this.target);
		}

		if (this.target == null)
		{
			this.onIdleTick();
		}
		else
		{
			this.componentMotion.addMotionTowards(this.transform, this.target.getTransform(), this.alertSpeed);

			this.onAlertTick();

			this.alertTicks.tick();
			if (this.alertTicks.isComplete())
			{
				this.target = null;
				this.onIdle();
			}
		}
	}
}
