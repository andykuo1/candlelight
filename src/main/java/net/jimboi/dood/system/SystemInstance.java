package net.jimboi.dood.system;

import net.jimboi.dood.base.EntityInstanceHandler;
import net.jimboi.dood.component.ComponentInstanceable;
import net.jimboi.dood.entity.Entity;
import net.jimboi.dood.entity.EntityManager;
import net.jimboi.mod.instance.InstanceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 5/23/17.
 */
public class SystemInstance extends EntitySystem implements EntityManager.OnEntityAddListener
{
	protected InstanceManager instanceManager;

	private List<EntityInstanceHandler> entities = new ArrayList<>();

	public SystemInstance(EntityManager entityManager, InstanceManager instanceManager)
	{
		super(entityManager);

		this.instanceManager = instanceManager;
	}

	@Override
	public void onStart()
	{
		this.registerListenable(entityManager.onEntityAdd);
	}

	@Override
	public void onStop()
	{
		this.clear();
	}

	@Override
	public void onEntityAdd(Entity entity)
	{
		if (entity.hasComponent(ComponentInstanceable.class))
		{
			EntityInstanceHandler handler = new EntityInstanceHandler(entity);
			this.entities.add(handler);
			this.instanceManager.add(handler);
		}
	}
}
