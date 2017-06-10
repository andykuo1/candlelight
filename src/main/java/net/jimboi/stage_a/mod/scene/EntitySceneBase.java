package net.jimboi.stage_a.mod.scene;

import net.jimboi.stage_a.dood.entity.EntityManager;

import org.qsilver.scene.Scene;

/**
 * Created by Andy on 5/30/17.
 */
public abstract class EntitySceneBase extends Scene
{
	protected final EntityManager entityManager;

	public EntitySceneBase()
	{
		this.entityManager = new EntityManager();
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.entityManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.entityManager.clear();
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}
}
