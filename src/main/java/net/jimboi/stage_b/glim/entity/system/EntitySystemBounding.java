package net.jimboi.stage_b.glim.entity.system;

import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.entity.component.EntityComponentBounding;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;

import org.joml.Vector3fc;
import org.zilar.entity.AbstractUpdateableEntitySystem;
import org.zilar.entity.Entity;
import org.zilar.entity.EntityManager;

import java.util.Collection;

/**
 * Created by Andy on 6/4/17.
 */
public class EntitySystemBounding extends AbstractUpdateableEntitySystem
{
	protected final BoundingManager boundingManager;

	public EntitySystemBounding(EntityManager entityManager, BoundingManager boundingManager)
	{
		super(entityManager);

		this.boundingManager = boundingManager;
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(EntityComponentTransform.class, EntityComponentBounding.class);

		for (Entity entity : entities)
		{
			EntityComponentTransform componentTransform = entity.getComponent(EntityComponentTransform.class);
			EntityComponentBounding componentBounding = entity.getComponent(EntityComponentBounding.class);
			Vector3fc pos = componentTransform.transform.position3();
			componentBounding.bounding.update(pos.x(), pos.y(), pos.z());
		}
	}
}
