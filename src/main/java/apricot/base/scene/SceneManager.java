package apricot.base.scene;

import apricot.base.render.OldRenderEngine;

import apricot.bstone.util.listener.Listenable;

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

	private boolean active = false;

	public void update(double delta)
	{
		if (!this.active) return;

		if (this.isSetupMode())
		{
			if (this.setupHandler == null)
			{
				if (this.currScene != null && this.currScene.sceneManager != this) throw new IllegalStateException("Unable to stop a scene that does not belong to this manager!");

				this.setupHandler = new SceneStopHandler(this.currScene, this.currScene == this.nextScene);
			}

			this.setupHandler.onUpdate(delta);

			if (this.setupHandler.isComplete())
			{
				if (this.setupHandler instanceof SceneStopHandler)
				{
					if (this.nextScene == null)
					{
						this.setupHandler = null;
						this.active = false;

						this.doSetup(false, null);

						return;
					}
					else
					{
						if (this.nextScene != null && this.nextScene.sceneManager != null && this.nextScene.sceneManager != this) throw new IllegalStateException("Unable to start a scene that already belongs to another manager!");

						this.setupHandler = new SceneStartHandler(this.nextScene, this.currScene == this.nextScene);

						this.currScene = null;
					}
				}
				else if (this.setupHandler instanceof SceneStartHandler)
				{
					this.setupHandler = null;

					Scene prevScene = this.currScene;
					this.doSetup(false, this.nextScene);

					this.onSceneChanged.notifyListeners(this.currScene, prevScene);
				}
				else
				{
					this.setupHandler = null;

					this.doSetup(false, null);
				}
			}
		}
		else if (this.currScene != null)
		{
			this.currScene.update(delta);
		}
	}

	public void renderUpdate(OldRenderEngine renderEngine)
	{
		if (this.isSetupMode())
		{
			if (this.setupHandler != null && !this.setupHandler.isComplete())
			{
				this.setupHandler.onRenderUpdate(renderEngine);
			}
		}
	}

	public void nextScene(Scene scene)
	{
		if (scene != null && scene.sceneManager != null && scene.sceneManager != this) throw new IllegalArgumentException("Invalid scene - scene already belongs to another manager!");

		this.active = true;

		this.doSetup(true, scene);
	}

	public void restartScene()
	{
		this.doSetup(true, this.currScene);
	}

	public void stopScene()
	{
		this.doSetup(true, null);
	}

	//TODO: This may cause confusion about when you can access this...
	public Scene getCurrentScene()
	{
		return this.currScene;
	}

	public boolean isActive()
	{
		return this.active;
	}

	public boolean isSetupMode()
	{
		return this.setup;
	}

	private void doSetup(boolean setup, Scene scene)
	{
		this.setup = setup;

		if (this.isSetupMode())
		{
			this.nextScene = scene;
		}
		else
		{
			this.currScene = scene;
			this.nextScene = null;
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
		public abstract void onRenderUpdate(OldRenderEngine renderEngine);

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
				this.scene.stop();
				this.sceneStopped = true;
			}
			else if (this.sceneUnloaded)
			{
				if (!this.sceneDestroyed)
				{
					this.scene.destroy();
					this.sceneDestroyed = true;
				}

				this.markComplete();
			}
		}

		@Override
		public void onRenderUpdate(OldRenderEngine renderEngine)
		{
			if (this.scene == null) return;

			if (this.sceneStopped)
			{
				if (!this.sceneUnloaded)
				{
					this.scene.unload(renderEngine);
					this.sceneUnloaded = true;
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
				this.scene.create(SceneManager.this);
				this.sceneCreated = true;
			}
			else if (this.sceneLoaded)
			{
				if (!this.sceneStarted)
				{
					this.scene.start();
					this.sceneStarted = true;
				}

				this.markComplete();
			}
		}

		@Override
		public void onRenderUpdate(OldRenderEngine renderEngine)
		{
			if (this.scene == null) return;

			if (this.sceneCreated)
			{
				if (!this.sceneLoaded)
				{
					this.scene.load(renderEngine);
					this.sceneLoaded = true;
				}
			}
		}
	}
}
