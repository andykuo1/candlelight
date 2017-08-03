package net.jimboi.apricot.base;

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
 * Created by Andy on 7/5/17.
 */
public final class GameEngine
{
	public static TickEngine TICKENGINE;
	public static Window WINDOW;
	public static InputEngine INPUTENGINE;
	public static RenderEngine RENDERENGINE;

	public static AssetManager ASSETMANAGER;
	public static SceneManager SCENEMANAGER;

	private GameEngine() {}

	@SuppressWarnings("unchecked")
	public static Scene init(String classpath, String[] args)
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

		return init(sceneClass, args);
	}

	public static Scene init(Class<? extends Scene> sceneClass, String[] args)
	{
		Poma.makeSystemLogger();

		Poma.div();
		Poma.info("The Awesome Program: " + sceneClass.getSimpleName());
		Poma.info("By: Andrew Kuo");
		Poma.info("Running program with args: " + Arrays.asList(args));
		Poma.div();

		WINDOW = new Window("Application", 640, 480);
		INPUTENGINE = WINDOW.getInputEngine();

		TICKENGINE = new TickEngine(60, true, new TickHandler()
		{
			@Override
			public void onFirstUpdate(TickEngine tickEngine)
			{
				RENDERENGINE.start();
			}

			@Override
			public void onPreUpdate()
			{
				if (!flag)
				{
					WINDOW.poll();

					if (WINDOW.shouldCloseWindow())
					{
						flag = true;
						SCENEMANAGER.stopScene();
					}
				}
			}

			@Override
			public void onFixedUpdate()
			{
				SCENEMANAGER.update(0.02D);
			}

			private boolean flag = false;

			@Override
			public void onUpdate(double delta)
			{
				if (!flag)
				{
					WINDOW.clearScreenBuffer();
				}
				else if (!SCENEMANAGER.isActive())
				{
					TICKENGINE.stop();
				}

				RENDERENGINE.update();

				if (!flag)
				{
					WINDOW.updateScreenBuffer();
				}
			}

			@Override
			public void onLastUpdate(TickEngine tickEngine)
			{
				ASSETMANAGER.destroy();
				RENDERENGINE.stop();
				WINDOW.destroy();
			}
		});

		RENDERENGINE = new RenderEngine(WINDOW);
		SCENEMANAGER = new SceneManager(RENDERENGINE);
		ASSETMANAGER = new AssetManager();

		RENDERENGINE.onRenderUpdate.addListener(ASSETMANAGER::update);

		Scene scene;
		try
		{
			scene = sceneClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new IllegalArgumentException("Invalid scene class! Must have defined default constructor!");
		}

		SCENEMANAGER.nextScene(scene);
		return scene;
	}

	public static void run()
	{
		if (TICKENGINE == null)
		{
			throw new IllegalStateException("Must call init first!");
		}

		TICKENGINE.run();
	}
}
