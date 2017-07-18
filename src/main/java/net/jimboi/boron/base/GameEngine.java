package net.jimboi.boron.base;

import org.bstone.input.InputEngine;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;
import org.qsilver.asset.AssetManager;
import org.qsilver.poma.Poma;
import org.qsilver.render.RenderEngine;
import org.qsilver.scene.Scene;
import org.qsilver.scene.SceneManager;

import java.util.Arrays;

/**
 * Created by Andy on 7/17/17.
 */
public final class GameEngine
{
	static
	{
		Poma.makeSystemLogger();
	}

	private final Class<? extends Scene> sceneClass;

	protected final TickEngine tickEngine;
	protected final Window window;
	protected final InputEngine inputEngine;
	protected final RenderEngine renderEngine;

	protected final AssetManager assetManager;
	protected final SceneManager sceneManager;

	public GameEngine(Class<? extends Scene> sceneClass, String[] args)
	{
		final String title = sceneClass.getSimpleName();
		this.sceneClass = sceneClass;

		Poma.div();
		Poma.info("The Wonderful Program: " + title);
		Poma.info("By: Andrew Kuo");
		Poma.info("Running program with args: " + Arrays.asList(args));
		Poma.div();

		this.window = new Window(title, 640, 480);
		this.inputEngine = this.window.getInputEngine();

		this.tickEngine = new TickEngine();
		this.renderEngine = new RenderEngine(this.window);
		this.sceneManager = new SceneManager(this.renderEngine);
		this.assetManager = new AssetManager();

		this.renderEngine.onRenderUpdate.addListener(this.assetManager::update);

		this.tickEngine.onFixedUpdate.addListener(this.sceneManager::update);
		this.tickEngine.onUpdate.addListener(this.renderEngine::update);
	}

	public GameEngine(String scenePath, String[] args)
	{
		this(getSceneClass(scenePath), args);
	}

	public void run()
	{
		Scene scene;
		try
		{
			scene = this.sceneClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new IllegalArgumentException("Invalid scene class! Must have defined default constructor!");
		}

		this.sceneManager.nextScene(scene);

		this.renderEngine.start();

		boolean flag = false;
		while(this.tickEngine.shouldKeepRunning())
		{
			if (!flag)
			{
				if (this.window.shouldCloseWindow())
				{
					flag = true;
					this.sceneManager.stopScene();
					continue;
				}

				this.window.update();
			}
			else if (!this.sceneManager.isActive())
			{
				this.tickEngine.stop();
			}

			this.tickEngine.update();

			if (!flag)
			{
				this.window.poll();
			}
		}

		this.assetManager.destroy();
		this.renderEngine.stop();
		this.window.destroy();
	}

	public TickEngine getTickEngine()
	{
		return this.tickEngine;
	}

	public Window getWindow()
	{
		return this.window;
	}

	public InputEngine getInputEngine()
	{
		return this.inputEngine;
	}

	public RenderEngine getRenderEngine()
	{
		return this.renderEngine;
	}

	public AssetManager getAssetManager()
	{
		return this.assetManager;
	}

	public SceneManager getSceneManager()
	{
		return this.sceneManager;
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends Scene> getSceneClass(String classpath)
	{
		Class<? extends Scene> sceneClass;
		try
		{
			Class<?> newclass = Class.forName(classpath);
			if (Scene.class.isAssignableFrom(newclass))
			{
				sceneClass = (Class<? extends Scene>) newclass;
			}
			else
			{
				throw new IllegalArgumentException("Not a scene! - Found invalid class with name: " + classpath);
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException("Unable to find class with name: " + classpath);
		}

		return sceneClass;
	}
}
