package org.zilar.base;

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
	public static void run(String classpath, String[] args)
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

		run(sceneClass, args);
	}

	public static void run(Class<? extends Scene> sceneClass, String[] args)
	{
		Poma.makeSystemLogger();

		Poma.div();
		Poma.info("The Awesome Program: " + sceneClass.getSimpleName());
		Poma.info("By: Andrew Kuo");
		Poma.info("Running program with args: " + Arrays.asList(args));
		Poma.div();

		TICKENGINE = new TickEngine();
		RENDERENGINE = new RenderEngine();
		SCENEMANAGER = new SceneManager(RENDERENGINE);
		ASSETMANAGER = new AssetManager();

		RENDERENGINE.onRenderUpdate.addListener(ASSETMANAGER::update);

		TICKENGINE.onFixedUpdate.addListener(SCENEMANAGER::update);
		TICKENGINE.onUpdate.addListener(RENDERENGINE::update);

		WINDOW = new Window("Application", 640, 480);
		INPUTENGINE = WINDOW.getInputEngine();

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

		RENDERENGINE.start();

		boolean flag = false;
		while(TICKENGINE.shouldKeepRunning())
		{
			if (!flag)
			{
				if (WINDOW.shouldCloseWindow())
				{
					flag = true;
					SCENEMANAGER.stopScene();
					continue;
				}

				WINDOW.update();
			}
			else if (!SCENEMANAGER.isActive())
			{
				TICKENGINE.stop();
			}

			TICKENGINE.update();

			if (!flag)
			{
				WINDOW.poll();
			}
		}

		ASSETMANAGER.destroy();
		RENDERENGINE.stop();
		WINDOW.destroy();
	}
}
