package net.jimboi.stage_c.hoob;

import org.qsilver.scene.Scene;
import org.zilar.bounding.BoundingManager;
import org.zilar.entity.AbstractEntitySystem;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityComponent;
import org.zilar.entity.EntityManager;

/**
 * Created by Andy on 7/15/17.
 */
public class EntitySystemBoundingCollider extends AbstractEntitySystem implements EntityManager.OnEntityComponentAddListener, EntityManager.OnEntityComponentRemoveListener
{
	private final BoundingManager boundingManager;

	public EntitySystemBoundingCollider(EntityManager entityManager, BoundingManager boundingManager)
	{
		super(entityManager);

		this.boundingManager = boundingManager;
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
			componentBoundingCollider.bounding = this.boundingManager.createCollider(componentBoundingCollider.shape);
		}
		else if (component instanceof EntityComponentBoundingStatic)
		{
			EntityComponentBoundingStatic componentBoundingStatic = (EntityComponentBoundingStatic) component;
			componentBoundingStatic.bounding = this.boundingManager.createStatic(componentBoundingStatic.shape);
		}
	}

	@Override
	public void onEntityComponentRemove(Entity entity, EntityComponent component)
	{
		if (component instanceof EntityComponentBoundingCollider)
		{
			EntityComponentBoundingCollider componentBoundingCollider = (EntityComponentBoundingCollider) component;
			this.boundingManager.destroyCollider(componentBoundingCollider.bounding);
		}
		else if (component instanceof EntityComponentBoundingStatic)
		{
			EntityComponentBoundingStatic componentBoundingStatic = (EntityComponentBoundingStatic) component;
			this.boundingManager.destroyStatic(componentBoundingStatic.bounding);
		}
	}

	public BoundingManager getBoundingManager()
	{
		return this.boundingManager;
	}
}
