package org.bstone.scene;

import org.bstone.render.RenderService;
import org.bstone.service.ServiceManager;
import org.bstone.util.pair.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by Andy on 10/20/17.
 */
public class SceneManager
{
	private final Map<String, Pair<Class<? extends Scene>, Class<? extends SceneRenderer>>> scenes = new HashMap<>();

	private Scene scene;
	private SceneRenderer renderer;
	private String renderServiceID;

	private volatile boolean performingSetup;
	private volatile boolean sceneDestroyed;
	private volatile boolean sceneLoaded;

	private String nextScene;
	private BiConsumer<Scene, SceneRenderer> nextCallback;

	private final ServiceManager<RenderService> renderServices;

	public SceneManager(ServiceManager<RenderService> renderServices)
	{
		this.renderServices = renderServices;
	}

	public void registerScene(String id, Class<? extends Scene> scene, Class<? extends SceneRenderer> renderer)
	{
		if (this.scenes.containsKey(id))
			throw new IllegalArgumentException("scene with id '" + id + "' already exists!");

		try
		{
			scene.getConstructor();
		}
		catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException("not a valid scene class - must have a default constructor");
		}

		try
		{
			renderer.getConstructor();
		}
		catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException("not a valid scene renderer class - must have a default constructor");
		}

		this.scenes.put(id, new Pair<>(scene, renderer));
	}

	public void unregisterScene(String id)
	{
		this.scenes.remove(id);
	}

	public void unregisterAll()
	{
		this.scenes.clear();
	}

	public void update()
	{
		//If queued to start a new scene...
		if (this.nextScene != null)
		{
			if (!this.performingSetup)
			{
				this.performingSetup = true;

				this.sceneDestroyed = false;
				this.sceneLoaded = false;
			}

			//Destroy the previous scene
			if (!this.sceneDestroyed)
			{
				if (this.scene != null)
				{
					this.scene.onSceneDestroy();
					this.scene = null;
				}

				this.sceneDestroyed = true;

				if (this.renderer != null)
				{
					this.renderServices.stopService(this.renderServiceID,
							renderService ->
							{
								this.renderer = null;
								this.renderServiceID = null;
							});
				}

				this.renderServices.startService(this.nextScene, this.scenes.get(this.nextScene).getSecond(),
						renderService -> {
							this.renderer = (SceneRenderer) renderService;
							this.renderServiceID = this.nextScene;
							this.sceneLoaded = true;
						});
				this.renderServices.pauseService(this.nextScene);
			}
			//Create the next scene
			else if (this.sceneLoaded)
			{
				if (this.scene != null)
				{
					throw new IllegalStateException("another scene is still active");
				}

				this.scene = this.createScene(this.nextScene);
				this.scene.onSceneCreate(this);

				//Start the scene
				if (this.nextCallback != null)
				{
					this.nextCallback.accept(this.scene, this.renderer);
				}
				this.renderServices.resumeService(this.nextScene);

				this.nextScene = null;
				this.nextCallback = null;

				this.performingSetup = false;
			}
		}
		else if (this.scene != null)
		{
			this.scene.onSceneUpdate();
		}
	}

	public void destroy()
	{
		if (this.nextScene != null) this.nextScene = null;
		if (this.scene != null)
		{
			this.scene.onSceneDestroy();
			this.scene = null;
		}
		if (this.renderer != null)
		{
			this.renderServices.stopService(this.renderServiceID);
			this.renderer = null;
			this.renderServiceID = null;
		}
	}

	private Scene createScene(String id)
	{
		final Pair<Class<? extends Scene>, Class<? extends SceneRenderer>> pair = this.scenes.get(id);
		final Class<? extends Scene> sceneClass = pair.getFirst();
		if (sceneClass == null) throw new IllegalArgumentException("could not instantiate scene with id '" + id + "'");

		Scene scene;
		try
		{
			scene = sceneClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new IllegalStateException("could not create scene with id '" + id + "'");
		}
		return scene;
	}

	public Scene getCurrentScene()
	{
		return this.scene;
	}

	public SceneRenderer getCurrentRenderer()
	{
		return this.renderer;
	}

	public void setNextScene(String id)
	{
		this.setNextScene(id, null);
	}

	/**
	 * Set the current scene to be disposed and start the next scene with
	 * passed-in id. Upon instantiation (but before creation) of the new scene,
	 * the callback will be processed.
	 * <p>The callback should NOT be used to instantiate the scene, it is just
	 * to modify or process a certain action in response to the event.
	 *
	 * @param id the scene id
	 * @param callback the function called on instantiation of the next scene
	 */
	public void setNextScene(String id, BiConsumer<Scene, SceneRenderer> callback)
	{
		if (!this.scenes.containsKey(id)) throw new IllegalArgumentException("could not find scene with id '" + id + "' - be sure to register it first!");

		this.nextScene = id;
		this.nextCallback = callback;
	}
}
