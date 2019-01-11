package canary.cuplet.scene_main.entity;

import canary.base.collisionbox.box.AxisAlignedBoundingBox;
import canary.base.collisionbox.response.CollisionResponse;
import canary.base.collisionbox.response.CollisionSolver;
import canary.cuplet.basicobject.ComponentRenderable;
import canary.cuplet.scene_main.GobletWorld;
import canary.cuplet.scene_main.component.ComponentMotion;
import canary.cuplet.scene_main.entity.base.EntityBase;
import canary.cuplet.scene_main.tick.TickCounter;
import canary.cuplet.scene_main.tile.TileMap;

import canary.bstone.transform.Transform3;

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

	@Override
	protected void onEntitySetup()
	{
		super.onEntitySetup();

		this.addComponent(new ComponentMotion());
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
	protected void onUpdate()
	{
		super.onUpdate();

		EntityPlayer entityPlayer = this.getWorld().getPlayer();
		TileMap tilemap = this.world.getRoom(this.transform.posX(), this.transform.posY());
		float dx = (this.transform.posX() - entityPlayer.getTransform().posX());
		float dy = (this.transform.posY() - entityPlayer.getTransform().posY());
		CollisionResponse response = CollisionSolver.solve(this.transform.posX(), this.transform.posY(), dx, dy, 0.3F, 0.3F, tilemap.getBoundingBox());
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
			this.getComponent(ComponentMotion.class).addMotionTowards(this.transform, this.target.getTransform(), this.alertSpeed);

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
