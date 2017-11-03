package net.jimboi.apricot.stage_a.mod.scene;

import net.jimboi.apricot.base.scene.Scene;
import net.jimboi.apricot.stage_a.dood.entity.EntityManager;

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
