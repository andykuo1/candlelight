package boron.stage_a.goblet.entity;

import boron.stage_a.base.collisionbox.collider.BoxCollider;
import boron.stage_a.base.collisionbox.response.CollisionResponse;
import boron.stage_a.goblet.GobletWorld;
import boron.stage_a.goblet.component.ComponentDamageable;
import boron.stage_a.goblet.component.ComponentMotion;
import boron.stage_a.goblet.tick.TickCounter;
import boron.stage_a.goblet.tile.TileMap;

import boron.bstone.entity.EntityManager;
import boron.bstone.transform.Transform3;
import org.joml.Vector3f;

/**
 * Created by Andy on 8/13/17.
 */
public class EntitySkeleton extends EntityAlertable
{
	protected final TickCounter shootTicks = new TickCounter(100);

	public EntitySkeleton(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.5F),
				world.createRenderable2D(transform, 'S', 0xAAAAAA));

		this.alertSpeed = 0.04F;
	}

	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		super.onEntityCreate(entityManager);

		this.getComponent(ComponentMotion.class).setFriction(0.3F);
		this.getComponent(ComponentDamageable.class).setMaxHealth(10);
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
		return collider instanceof TileMap;
	}

	@Override
	public boolean isColliderActive()
	{
		return true;
	}
}
