package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionSolver;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.Room;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/11/17.
 */
public class EntityRat extends EntityHurtable implements ActiveBoxCollider
{
	private EntityBase target;
	private int maxAlertTicks = 50;
	private int alertTicks;

	public EntityRat(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.4F),
				world.createRenderable2D(transform, 'R', 0xDDDDDD));

		this.maxHealth = 5;
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		EntityPlayer entityPlayer = (EntityPlayer) this.getWorld().getPlayer();
		Room room = this.world.getRoom(this.transform.posX(), this.transform.posY());
		float dx = (this.transform.posX() - entityPlayer.getTransform().posX());
		float dy = (this.transform.posY() - entityPlayer.getTransform().posY());
		CollisionResponse response = CollisionSolver.solve(this.transform.posX(), this.transform.posY(), dx, dy, 0.3F, 0.3F, room.getBoundingBox());
		if (response == null)
		{
			this.target = entityPlayer;
			this.alertTicks = this.maxAlertTicks;
		}

		if (this.target == null)
		{
			this.motion.set(0);
		}
		else
		{
			this.motion.set(this.target.getTransform().posX() - this.transform.posX(), this.target.getTransform().posY() - this.transform.posY()).normalize().mul(0.02F);

			--this.alertTicks;
			if (this.alertTicks < 0)
			{
				this.target = null;
				this.alertTicks = 0;
			}
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
