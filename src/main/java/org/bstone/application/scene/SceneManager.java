package org.bstone.application.scene;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Andy on 10/20/17.
 */
public class SceneManager
{
	private final Map<String, Class<? extends Scene>> scenes = new HashMap<>();

	private Scene scene;

	private String nextScene;
	private Consumer<Scene> nextCallback;

	public void registerScene(String id, Class<? extends Scene> scene)
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

		this.scenes.put(id, scene);
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
			//Destroy previous scene
			if (this.scene != null)
			{
				this.scene.onSceneDestroy();
				this.scene = null;
			}

			//Create next scene
			this.scene = this.createScene(this.nextScene);
			if (this.nextCallback != null)
			{
				this.nextCallback.accept(this.scene);
			}
			this.scene.onSceneCreate(this);

			this.nextScene = null;
			this.nextCallback = null;
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
	}

	private Scene createScene(String id)
	{
		final Class<? extends Scene> sceneClass = this.scenes.get(id);
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
	public void setNextScene(String id, Consumer<Scene> callback)
	{
		if (!this.scenes.containsKey(id)) throw new IllegalArgumentException("could not find scene with id '" + id + "' - be sure to register it first!");

		this.nextScene = id;
		this.nextCallback = callback;
	}
}
