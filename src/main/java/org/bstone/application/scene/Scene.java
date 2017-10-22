package org.bstone.application.scene;

/**
 * Created by Andy on 10/20/17.
 */
public abstract class Scene
{
	protected abstract void onSceneCreate(SceneManager sceneManager);

	protected abstract void onSceneUpdate();

	protected abstract void onSceneDestroy();
}
