package org.qsilver.scene;

import org.bstone.util.listener.Listenable;
import org.qsilver.poma.Poma;

/**
 * Created by Andy on 3/1/17.
 */
public class SceneManager
{
	public interface OnSceneChanged
	{
		void onSceneChanged(Scene scene, Scene prevScene);
	}

	public final Listenable<OnSceneChanged> onSceneChanged = new Listenable<>((listener, objects) -> listener.onSceneChanged((Scene) objects[0], (Scene) objects[1]));

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
					Scene prevScene = this.currScene;
					this.currScene = this.nextScene;
					this.nextScene = null;
					this.setup = false;

					this.onSceneChanged.notifyListeners(this.currScene, prevScene);
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
			this.currScene.onSceneUpdate(delta);
			this.currScene.onSceneUpdate.notifyListeners(delta);
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
				this.scene.onSceneStop.notifyListeners();
			}
			else if (this.sceneUnloaded)
			{
				if (!this.sceneDestroyed)
				{
					Poma.d("Destroying Scene. . .");
					this.scene.onSceneDestroy();
					this.sceneDestroyed = true;
					this.scene.onSceneDestroy.notifyListeners();
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
					this.scene.onSceneUnload.notifyListeners();
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
				this.scene.onSceneCreate.notifyListeners();
			}
			else if (this.sceneLoaded)
			{
				if (!this.sceneStarted)
				{
					Poma.d("Starting Scene. . .");
					this.scene.onSceneStart();
					this.sceneStarted = true;
					this.scene.onSceneStart.notifyListeners();
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
					this.scene.onSceneLoad.notifyListeners();
				}
			}
		}
	}
}
