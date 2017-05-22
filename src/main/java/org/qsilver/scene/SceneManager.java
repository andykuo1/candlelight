package org.qsilver.scene;

import org.bstone.poma.Poma;
import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 3/1/17.
 */
public class SceneManager
{
	public interface OnSceneCreateListener
	{
		void onSceneCreate(Scene scene);
	}

	public interface OnSceneLoadListener
	{
		void onSceneLoad(Scene scene);
	}

	public interface OnSceneStartListener
	{
		void onSceneStart(Scene scene);
	}

	public interface OnSceneStopListener
	{
		void onSceneStop(Scene scene);
	}

	public interface OnSceneUnloadListener
	{
		void onSceneUnload(Scene scene);
	}

	public interface OnSceneDestroyListener
	{
		void onSceneDestroy(Scene scene);
	}

	public final Listenable<OnSceneCreateListener> onSceneCreate = new Listenable<>((listener, objects) -> listener.onSceneCreate((Scene) objects[0]));
	public final Listenable<OnSceneLoadListener> onSceneLoad = new Listenable<>((listener, objects) -> listener.onSceneLoad((Scene) objects[0]));
	public final Listenable<OnSceneStartListener> onSceneStart = new Listenable<>((listener, objects) -> listener.onSceneStart((Scene) objects[0]));
	public final Listenable<OnSceneStopListener> onSceneStop = new Listenable<>((listener, objects) -> listener.onSceneStop((Scene) objects[0]));
	public final Listenable<OnSceneUnloadListener> onSceneUnload = new Listenable<>((listener, objects) -> listener.onSceneUnload((Scene) objects[0]));
	public final Listenable<OnSceneDestroyListener> onSceneDestroy = new Listenable<>((listener, objects) -> listener.onSceneDestroy((Scene) objects[0]));

	private Scene nextScene;
	private Scene currScene;

	private boolean setup = false;
	private SceneSetupHandler setupHandler;

	public SceneManager()
	{
	}

	public void update(double delta)
	{
		if (this.setup)
		{
			if (this.setupHandler == null)
			{
				this.setupHandler = new SceneStopHandler(this.currScene, this.currScene == this.nextScene);
			}

			this.setupHandler.onUpdate(delta);

			if (this.setupHandler.isComplete())
			{
				if (this.setupHandler instanceof SceneStopHandler)
				{
					this.setupHandler = new SceneStartHandler(this.nextScene, this.currScene == this.nextScene);
					this.currScene = null;
				}
				else if (this.setupHandler instanceof SceneStartHandler)
				{
					this.setupHandler = null;
					this.currScene = this.nextScene;
					this.nextScene = null;
					this.setup = false;
				}
				else
				{
					this.setupHandler = null;
					this.currScene = null;
					this.nextScene = null;
					this.setup = false;
				}
			}
		}
		else if (this.currScene != null)
		{
			this.currScene.onUpdate(delta);
		}
	}

	public void render()
	{
		if (this.setup)
		{
			if (this.setupHandler != null && !this.setupHandler.isComplete())
			{
				this.setupHandler.onRender();
			}
		}
		else if (this.currScene != null)
		{
			this.currScene.onRender();
		}
	}

	public void nextScene(Scene scene)
	{
		this.nextScene = scene;
		this.setup = true;
	}

	public void restartScene()
	{
		this.nextScene = this.currScene;
		this.setup = true;
	}

	public void clearScene()
	{
		this.nextScene = null;
		this.setup = true;
	}

	public void clear()
	{
		this.clearScene();

		while(this.setup || this.currScene != null)
		{
			this.update(0);
			this.render();
		}
	}

	private abstract class SceneSetupHandler
	{
		protected final Scene scene;
		private boolean complete;

		public SceneSetupHandler(Scene scene)
		{
			this.scene = scene;

			this.complete = this.scene == null;
		}

		public abstract void onUpdate(double delta);
		public abstract void onRender();

		protected synchronized void markComplete()
		{
			this.complete = true;
		}

		public synchronized boolean isComplete()
		{
			return this.complete;
		}
	}

	private class SceneStopHandler extends SceneSetupHandler
	{
		private boolean sceneStopped = false;
		private boolean sceneUnloaded = false;
		private boolean sceneDestroyed = false;

		public SceneStopHandler(Scene scene, boolean restart)
		{
			super(scene);

			this.sceneDestroyed = restart;
		}

		@Override
		public void onUpdate(double delta)
		{
			if (this.scene == null) return;

			if (!this.sceneStopped)
			{
				Poma.d("Stopping Scene. . .");
				this.scene.onSceneStop();
				this.sceneStopped = true;
				SceneManager.this.onSceneStop.notifyListeners(this.scene);
			}
			else if (this.sceneUnloaded)
			{
				if (!this.sceneDestroyed)
				{
					Poma.d("Destroying Scene. . .");
					this.scene.onSceneDestroy();
					this.sceneDestroyed = true;
					SceneManager.this.onSceneDestroy.notifyListeners(this.scene);
				}
				else
				{
					this.markComplete();
				}
			}
		}

		@Override
		public void onRender()
		{
			if (this.scene == null) return;

			if (this.sceneStopped)
			{
				if (!this.sceneUnloaded)
				{
					Poma.d("Unloading Scene. . .");
					this.scene.onSceneUnload();
					this.sceneUnloaded = true;
					SceneManager.this.onSceneUnload.notifyListeners(this.scene);
				}
			}
		}
	}

	private class SceneStartHandler extends SceneSetupHandler
	{
		private boolean sceneCreated = false;
		private boolean sceneLoaded = false;
		private boolean sceneStarted = false;

		public SceneStartHandler(Scene scene, boolean restart)
		{
			super(scene);

			this.sceneCreated = restart;
		}

		@Override
		public void onUpdate(double delta)
		{
			if (this.scene == null) return;

			if (!this.sceneCreated)
			{
				Poma.d("Creating Scene. . .");
				this.scene.onSceneCreate();
				this.sceneCreated = true;
				SceneManager.this.onSceneCreate.notifyListeners(this.scene);
			}
			else if (this.sceneLoaded)
			{
				if (!this.sceneStarted)
				{
					Poma.d("Starting Scene. . .");
					this.scene.onSceneStart();
					this.sceneStarted = true;
					SceneManager.this.onSceneStart.notifyListeners(this.scene);
				}
				else
				{
					this.markComplete();
				}
			}
		}

		@Override
		public void onRender()
		{
			if (this.scene == null) return;

			if (this.sceneCreated)
			{
				if (!this.sceneLoaded)
				{
					Poma.d("Loading Scene. . .");
					this.scene.onSceneLoad();
					this.sceneLoaded = true;
					SceneManager.this.onSceneLoad.notifyListeners(this.scene);
				}
			}
		}
	}
}
