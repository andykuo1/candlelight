package net.jimboi.stage_b.glim.gameentity.system;

import net.jimboi.stage_b.glim.gameentity.component.GameComponentInstance;
import net.jimboi.stage_b.gnome.instance.InstanceManager;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;

/**
 * Created by Andy on 6/1/17.
 */
public class EntitySystemInstance extends EntitySystemBase implements EntityManager.OnEntityAddListener, EntityManager.OnEntityRemoveListener
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
		if (entity.hasComponent(GameComponentInstance.class))
		{
			GameComponentInstance c_instance = entity.getComponent(GameComponentInstance.class);
			this.instanceManager.add(c_instance);
		}
	}

	@Override
	public void onEntityRemove(Entity entity)
	{
		if (entity.hasComponent(GameComponentInstance.class))
		{
			GameComponentInstance c_instance = entity.getComponent(GameComponentInstance.class);
			this.instanceManager.remove(c_instance);
		}
	}
}
