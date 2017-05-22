package org.qsilver.scene;

/**
 * Created by Andy on 3/1/17.
 */
public abstract class Scene
{
	protected abstract void onSceneCreate();

	protected abstract void onSceneLoad();

	protected abstract void onSceneStart();

	protected abstract void onUpdate(double delta);

	protected abstract void onRender();

	protected abstract void onSceneStop();

	protected abstract void onSceneUnload();

	protected abstract void onSceneDestroy();
}
