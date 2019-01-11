package boron.stage_a.goblet.entity;

import boron.stage_a.base.collisionbox.collider.BoxCollider;
import boron.stage_a.base.collisionbox.response.CollisionResponse;
import boron.stage_a.goblet.GobletWorld;
import boron.stage_a.goblet.component.ComponentDamageable;
import boron.stage_a.goblet.component.ComponentMotion;
import boron.stage_a.goblet.tile.TileMap;

import boron.bstone.entity.EntityManager;
import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 8/11/17.
 */
public class EntityRat extends EntityAlertable
{
	public EntityRat(GobletWorld world, Transform3 transform)
	{
		super(world, transform,
				world.createBoundingBox(transform, 0.4F),
				world.createRenderable2D(transform, 'R', 0xDDDDDD));
	}

	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		super.onEntityCreate(entityManager);

		this.getComponent(ComponentMotion.class).setFriction(0.3F);
		this.getComponent(ComponentDamageable.class).setMaxHealth(1);
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
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
