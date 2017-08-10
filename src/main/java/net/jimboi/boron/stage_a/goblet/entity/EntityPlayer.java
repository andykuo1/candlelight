package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.goblet.Goblet;
import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityPlayer extends EntityMotion implements ActiveBoxCollider
{
	private float speed;

	public EntityPlayer(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 1),
				world.createRenderable2D(transform, '@', 0xFF00FF));

		this.speed = 0.1F;
		this.friction = 0.2F;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (Goblet.getGoblet().getInput().isInputDown("left"))
		{
			this.motion.x = -this.speed;
		}
		if (Goblet.getGoblet().getInput().isInputDown("right"))
		{
			this.motion.x = this.speed;
		}
		if (Goblet.getGoblet().getInput().isInputDown("up"))
		{
			this.motion.y = this.speed;
		}
		if (Goblet.getGoblet().getInput().isInputDown("down"))
		{
			this.motion.y = -this.speed;
		}
		if (Goblet.getGoblet().getInput().isInputReleased("mouseleft"))
		{
			this.world.spawnEntity(new EntityRock(this.world, this.world.createTransform(this.transform), 0.03F * (float) (Math.random() - 0.5F), 0.03F * (float) (Math.random() - 0.5F), 0.2F));
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
		return collider instanceof EntitySolid && !((EntitySolid) collider).isDead();
	}
}
