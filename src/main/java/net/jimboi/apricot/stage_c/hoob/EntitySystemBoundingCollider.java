package net.jimboi.apricot.stage_c.hoob;

import net.jimboi.boron.stage_a.shroom.woot.collision.CollisionManager;

import org.qsilver.scene.Scene;
import org.zilar.entity.AbstractEntitySystem;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityComponent;
import org.zilar.entity.EntityManager;

/**
 * Created by Andy on 7/15/17.
 */
public class EntitySystemBoundingCollider extends AbstractEntitySystem implements EntityManager.OnEntityComponentAddListener, EntityManager.OnEntityComponentRemoveListener
{
	private final CollisionManager collisionManager;

	public EntitySystemBoundingCollider(EntityManager entityManager, CollisionManager boundingManager)
	{
		super(entityManager);

		this.collisionManager = boundingManager;
	}

	@Override
	protected void onStart(Scene scene)
	{
		this.entityManager.onEntityComponentAdd.addListener(this);
		this.entityManager.onEntityComponentRemove.addListener(this);
	}

	@Override
	protected void onStop(Scene scene)
	{
		this.entityManager.onEntityComponentAdd.deleteListener(this);
		this.entityManager.onEntityComponentRemove.deleteListener(this);
	}

	@Override
	public void onEntityComponentAdd(Entity entity, EntityComponent component)
	{
		if (component instanceof EntityComponentBoundingCollider)
		{
			EntityComponentBoundingCollider componentBoundingCollider = (EntityComponentBoundingCollider) component;
			componentBoundingCollider.collider = this.collisionManager.createCollider(componentBoundingCollider.shape);
		}
		else if (component instanceof EntityComponentBoundingStatic)
		{
			EntityComponentBoundingStatic componentBoundingStatic = (EntityComponentBoundingStatic) component;
			componentBoundingStatic.collider = this.collisionManager.createStatic(componentBoundingStatic.shape);
		}
	}

	@Override
	public void onEntityComponentRemove(Entity entity, EntityComponent component)
	{
		if (component instanceof EntityComponentBoundingCollider)
		{
			EntityComponentBoundingCollider componentBoundingCollider = (EntityComponentBoundingCollider) component;
			this.collisionManager.destroyCollider(componentBoundingCollider.collider);
		}
		else if (component instanceof EntityComponentBoundingStatic)
		{
			EntityComponentBoundingStatic componentBoundingStatic = (EntityComponentBoundingStatic) component;
			this.collisionManager.destroyStatic(componentBoundingStatic.collider);
		}
	}

	public CollisionManager getCollisionManager()
	{
		return this.collisionManager;
	}
}
