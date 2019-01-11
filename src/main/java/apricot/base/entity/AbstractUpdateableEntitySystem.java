package apricot.base.entity;

import apricot.base.scene.Scene;

/**
 * Created by Andy on 7/6/17.
 */
public abstract class AbstractUpdateableEntitySystem extends AbstractEntitySystem implements Scene.OnSceneUpdateListener
{
	public AbstractUpdateableEntitySystem(EntityManager entityManager)
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
