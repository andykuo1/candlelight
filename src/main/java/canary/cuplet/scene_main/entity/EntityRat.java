package canary.cuplet.scene_main.entity;

import canary.base.collisionbox.collider.BoxCollider;
import canary.base.collisionbox.response.CollisionResponse;
import canary.cuplet.scene_main.GobletWorld;
import canary.cuplet.scene_main.component.ComponentDamageable;
import canary.cuplet.scene_main.component.ComponentMotion;
import canary.cuplet.scene_main.tile.TileMap;

import canary.bstone.transform.Transform3;

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
	protected void onEntitySetup()
	{
		super.onEntitySetup();

		this.getComponent(ComponentMotion.class).setFriction(0.3F);
		this.getComponent(ComponentDamageable.class).setMaxHealth(1);
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();
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
