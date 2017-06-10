package net.jimboi.stage_a.dood.system;

import net.jimboi.stage_a.dood.base.EntityInstanceHandler;
import net.jimboi.stage_a.dood.component.ComponentInstanceable;
import net.jimboi.stage_a.dood.entity.Entity;
import net.jimboi.stage_a.dood.entity.EntityManager;
import net.jimboi.stage_a.mod.instance.InstanceManager;

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
