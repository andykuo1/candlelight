package org.qsilver.scene;

import org.bstone.util.listener.Listenable;
import org.qsilver.poma.Poma;
import org.qsilver.renderer.RenderEngine;
import org.qsilver.service.ServiceManager;

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
	public final Listenable<OnSceneStopListener> onSceneStop = new Listenable<>((listener, objects) -> listener.onSceneStop());
	public final Listenable<OnSceneUnloadListener> onSceneUnload = new Listenable<>((listener, objects) -> listener.onSceneUnload());
	public final Listenable<OnSceneDestroyListener> onSceneDestroy = new Listenable<>((listener, objects) -> listener.onSceneDestroy());

	SceneManager sceneManager;
	ServiceManager<Scene> serviceManager;

	void create(SceneManager sceneManager)
	{
		Poma.div();
		System.out.println("Creating Scene. . .");
		this.sceneManager = sceneManager;
		this.serviceManager = new ServiceManager<>(this);
		this.onSceneCreate();
		this.onSceneCreate.notifyListeners();
	}

	void load(RenderEngine renderEngine)
	{
		Poma.div();
		System.out.println("Loading Scene. . .");
		this.onSceneLoad(renderEngine);
		this.onSceneLoad.notifyListeners();
	}

	void start()
	{
		Poma.div();
		System.out.println("Starting Scene. . .");
		this.serviceManager.beginServiceBlock();
		this.onSceneStart();
		this.onSceneStart.notifyListeners();
		this.serviceManager.endServiceBlock();

		this.firstUpdate = true;
	}

	private boolean firstUpdate = true;

	void update(double delta)
	{
		if (this.firstUpdate)
		{
			Poma.div();
			this.firstUpdate = false;
		}

		this.serviceManager.beginServiceBlock();
		this.onSceneUpdate(delta);
		this.onSceneUpdate.notifyListeners(delta);
		this.serviceManager.endServiceBlock();
	}

	void stop()
	{
		Poma.div();
		System.out.println("Stopping Scene. . .");
		this.serviceManager.beginServiceBlock();
		this.onSceneStop();
		this.onSceneStop.notifyListeners();
		this.serviceManager.endServiceBlock();
	}

	void unload(RenderEngine renderEngine)
	{
		Poma.div();
		System.out.println("Unloading Scene. . .");
		this.onSceneUnload(renderEngine);
		this.onSceneUnload.notifyListeners();
	}

	void destroy()
	{
		Poma.div();
		System.out.println("Destroying Scene. . .");
		this.onSceneDestroy();
		this.onSceneDestroy.notifyListeners();
		this.sceneManager = null;
		this.serviceManager.clear();
		this.serviceManager = null;
	}

	protected abstract void onSceneCreate();

	protected abstract void onSceneLoad(RenderEngine renderManager);

	protected abstract void onSceneStart();

	protected abstract void onSceneUpdate(double delta);

	protected abstract void onSceneStop();

	protected abstract void onSceneUnload(RenderEngine renderManager);

	protected abstract void onSceneDestroy();

	public final SceneManager getSceneManager()
	{
		return this.sceneManager;
	}

	public final ServiceManager<Scene> getServiceManager()
	{
		return this.serviceManager;
	}
}
