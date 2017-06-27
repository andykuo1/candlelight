package net.jimboi.base;

import net.jimboi.stage_b.glim.SceneGlim;

import org.bstone.input.InputEngine;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;
import org.qsilver.poma.Poma;
import org.qsilver.renderer.RenderEngine;
import org.qsilver.scene.Scene;
import org.qsilver.scene.SceneManager;

import java.util.Arrays;

/**
 * Created by Andy on 4/27/17.
 */
public class Main
{
	public static Scene SCENE;

	public static TickEngine TICKENGINE;
	public static Window WINDOW;
	public static InputEngine INPUTENGINE;
	public static RenderEngine RENDERENGINE;

	public static SceneManager SCENEMANAGER;

	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		Poma.makeSystemLogger();

		Class<? extends Scene> scene = SceneGlim.class;

		if (args.length > 0)
		{
			String classpath = args[0];
			try
			{
				Class<?> newclass = Class.forName(classpath);
				if (newclass.isAssignableFrom(Scene.class))
				{
					scene = (Class<? extends Scene>) newclass;
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
		}

		Poma.div();
		Poma.info("The Awesome Program: " + scene.getSimpleName());
		Poma.info("By: Andrew Kuo");
		Poma.info("Running program with args: " + Arrays.asList(args));
		Poma.div();

		SCENEMANAGER = new SceneManager();

		RENDERENGINE = new RenderEngine();

		TICKENGINE = new TickEngine();
		TICKENGINE.onFixedUpdate.addListener(SCENEMANAGER::update);
		TICKENGINE.onUpdate.addListener(() ->
		{
			SCENEMANAGER.renderUpdate(RENDERENGINE);
			RENDERENGINE.update();
		});

		WINDOW = new Window("Application", 640, 480);
		INPUTENGINE = WINDOW.getInputEngine();

		try
		{
			SCENE = scene.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new IllegalArgumentException("Invalid scene class! Must have defined default constructor!");
		}

		SCENEMANAGER.nextScene(SCENE);

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

		RENDERENGINE.stop();
		WINDOW.destroy();
	}
}
