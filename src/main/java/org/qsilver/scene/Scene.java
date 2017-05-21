package org.qsilver.scene;

/**
 * Created by Andy on 3/1/17.
 */
public abstract class Scene
{
	public abstract void onSceneCreate();
	public abstract void onSceneLoad();
	public abstract void onSceneStart();
	public abstract void onUpdate(double delta);
	public abstract void onRender();
	public abstract void onSceneStop();
	public abstract void onSceneUnload();
	public abstract void onSceneDestroy();
}
