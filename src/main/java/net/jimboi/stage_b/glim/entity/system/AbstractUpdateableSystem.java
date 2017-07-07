package net.jimboi.stage_b.glim.entity.system;

import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 7/6/17.
 */
public abstract class AbstractUpdateableSystem extends AbstractSystem implements Scene.OnSceneUpdateListener
{
	public AbstractUpdateableSystem(EntityManager entityManager)
	{
		super(entityManager);
	}

	@Override
	protected void onStart(Scene scene)
	{
		scene.onSceneUpdate.addListener(this);
	}

	@Override
	protected void onStop(Scene scene)
	{
		scene.onSceneUpdate.deleteListener(this);
	}
}
