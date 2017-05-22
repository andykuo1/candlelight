package org.qsilver.scene;

import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 3/1/17.
 */
public abstract class Scene
{
	public interface OnSceneCreateListener
	{
		void onSceneCreate();
	}

	public interface OnSceneLoadListener
	{
		void onSceneLoad();
	}

	public interface OnSceneStartListener
	{
		void onSceneStart();
	}

	public interface OnSceneUpdateListener
	{
		void onSceneUpdate(double delta);
	}

	public interface OnSceneRenderListener
	{
		void onSceneRender();
	}

	public interface OnSceneStopListener
	{
		void onSceneStop();
	}

	public interface OnSceneUnloadListener
	{
		void onSceneUnload();
	}

	public interface OnSceneDestroyListener
	{
		void onSceneDestroy();
	}

	public final Listenable<OnSceneCreateListener> onSceneCreate = new Listenable<>((listener, objects) -> listener.onSceneCreate());
	public final Listenable<OnSceneLoadListener> onSceneLoad = new Listenable<>((listener, objects) -> listener.onSceneLoad());
	public final Listenable<OnSceneStartListener> onSceneStart = new Listenable<>((listener, objects) -> listener.onSceneStart());
	public final Listenable<OnSceneUpdateListener> onSceneUpdate = new Listenable<>((listener, objects) -> listener.onSceneUpdate((Double) objects[0]));
	public final Listenable<OnSceneRenderListener> onSceneRender = new Listenable<>((listener, objects) -> listener.onSceneRender());
	public final Listenable<OnSceneStopListener> onSceneStop = new Listenable<>((listener, objects) -> listener.onSceneStop());
	public final Listenable<OnSceneUnloadListener> onSceneUnload = new Listenable<>((listener, objects) -> listener.onSceneUnload());
	public final Listenable<OnSceneDestroyListener> onSceneDestroy = new Listenable<>((listener, objects) -> listener.onSceneDestroy());

	protected abstract void onSceneCreate();

	protected abstract void onSceneLoad();

	protected abstract void onSceneStart();

	protected abstract void onSceneUpdate(double delta);

	protected abstract void onSceneRender();

	protected abstract void onSceneStop();

	protected abstract void onSceneUnload();

	protected abstract void onSceneDestroy();
}
