package net.jimboi.boron.base;

import org.bstone.input.InputEngine;
import org.bstone.tick.TickEngine;
import org.bstone.tick.TickHandler;
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

	protected final TickEngine tickEngine;
	protected final Window window;
	protected final InputEngine inputEngine;
	protected final RenderEngine renderEngine;

	protected final AssetManager assetManager;
	protected final SceneManager sceneManager;

	private Class<? extends Scene> sceneClass;

	public GameEngine(String[] args)
	{
		Poma.div();
		Poma.info("The Great Engine");
		Poma.info("By: Andrew Kuo");
		Poma.info("Running program with args: " + Arrays.asList(args));
		Poma.div();

		this.window = new Window("Demo", 640, 480);
		this.inputEngine = this.window.getInputEngine();

		this.tickEngine = new TickEngine(60, true, new TickHandler()
		{
			@Override
			public void onFirstUpdate(TickEngine tickEngine)
			{
				renderEngine.start();
			}

			@Override
			public void onPreUpdate()
			{
				if (!flag)
				{
					window.poll();

					if (window.shouldCloseWindow())
					{
						flag = true;
						sceneManager.stopScene();
					}
				}
			}

			@Override
			public void onFixedUpdate()
			{
				sceneManager.update(0.02D);
			}

			private boolean flag = false;

			@Override
			public void onUpdate(double delta)
			{
				if (!flag)
				{
					window.clearScreenBuffer();
				}
				else if (!sceneManager.isActive())
				{
					tickEngine.stop();
				}

				renderEngine.update();

				if (!flag)
				{
					window.updateScreenBuffer();
				}
			}

			@Override
			public void onLastUpdate(TickEngine tickEngine)
			{
				assetManager.destroy();
				renderEngine.stop();
				window.destroy();
			}
		});

		this.renderEngine = new RenderEngine(this.window);
		this.sceneManager = new SceneManager(this.renderEngine);
		this.assetManager = new AssetManager();

		this.renderEngine.onRenderUpdate.addListener(this.assetManager::update);
	}

	public void run(String scenePath)
	{
		this.run(getSceneClass(scenePath));
	}

	public void run(Class<? extends Scene> sceneClass)
	{
		this.sceneClass = sceneClass;

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

		this.tickEngine.run();
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
