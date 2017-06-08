package net.jimboi.glim.system;

import net.jimboi.dood.system.EntitySystem;
import net.jimboi.glim.component.EntityComponentInstance;
import net.jimboi.mod.instance.InstanceManager;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;

/**
 * Created by Andy on 6/1/17.
 */
public class EntitySystemInstance extends EntitySystem implements EntityManager.OnEntityAddListener, EntityManager.OnEntityRemoveListener
{
	protected final InstanceManager instanceManager;

	public EntitySystemInstance(EntityManager entityManager, InstanceManager instanceManager)
	{
		super(entityManager);

		this.instanceManager = instanceManager;

		this.registerListenable(entityManager.onEntityAdd);
		this.registerListenable(entityManager.onEntityRemove);
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	@Override
	public void onEntityAdd(Entity entity)
	{
		if (entity.hasComponent(EntityComponentInstance.class))
		{
			EntityComponentInstance c_instance = entity.getComponent(EntityComponentInstance.class);
			this.instanceManager.add(c_instance);
		}
	}

	@Override
	public void onEntityRemove(Entity entity)
	{
		if (entity.hasComponent(EntityComponentInstance.class))
		{
			EntityComponentInstance c_instance = entity.getComponent(EntityComponentInstance.class);
			this.instanceManager.remove(c_instance);
		}
	}
}
