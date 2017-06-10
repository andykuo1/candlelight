package net.jimboi.stage_b.glim.system;

import net.jimboi.stage_b.glim.gameentity.GameEntity;
import net.jimboi.stage_b.glim.gameentity.GameEntityManager;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentInstance;
import net.jimboi.stage_b.gnome.instance.InstanceManager;

/**
 * Created by Andy on 6/1/17.
 */
public class EntitySystemInstance extends EntitySystemBase implements GameEntityManager.OnGameEntityAddListener, GameEntityManager.OnGameEntityRemoveListener
{
	protected final InstanceManager instanceManager;

	public EntitySystemInstance(GameEntityManager entityManager, InstanceManager instanceManager)
	{
		super(entityManager);

		this.instanceManager = instanceManager;

		this.registerListenable(entityManager.onGameEntityAdd);
		this.registerListenable(entityManager.onGameEntityRemove);
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
	public void onGameEntityAdd(GameEntity entity)
	{
		if (entity.hasComponent(GameComponentInstance.class))
		{
			GameComponentInstance c_instance = entity.getComponent(GameComponentInstance.class);
			this.instanceManager.add(c_instance);
		}
	}

	@Override
	public void onGameEntityRemove(GameEntity entity)
	{
		if (entity.hasComponent(GameComponentInstance.class))
		{
			GameComponentInstance c_instance = entity.getComponent(GameComponentInstance.class);
			this.instanceManager.remove(c_instance);
		}
	}
}
