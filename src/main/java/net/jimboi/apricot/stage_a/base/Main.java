package net.jimboi.apricot.stage_a.base;

import net.jimboi.apricot.stage_a.dood.SceneDood;

import org.bstone.input.InputEngine;
import org.bstone.tick.TickEngine;
import org.bstone.tick.TickHandler;
import org.bstone.window.Window;
import org.qsilver.poma.Poma;
import org.qsilver.render.RenderEngine;
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

		Class<? extends Scene> scene = SceneDood.class;

		if (args.length > 0)
		{
			String classpath = args[0];
			try
			{
				Class<?> newclass = Class.forName(classpath);
				if (Scene.class.isAssignableFrom(newclass))
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

		WINDOW = new Window("Application", 640, 480);
		INPUTENGINE = WINDOW.getInputEngine();

		TICKENGINE = new TickEngine(30, true, new TickHandler()
		{
			@Override
			public void onFirstUpdate(TickEngine tickEngine)
			{

			}

			@Override
			public void onPreUpdate()
			{

			}

			@Override
			public void onFixedUpdate()
			{
				SCENEMANAGER.update(1);
			}

			private boolean flag = false;

			@Override
			public void onUpdate(double delta)
			{
				if (!flag)
				{
					if (WINDOW.shouldCloseWindow())
					{
						flag = true;
						SCENEMANAGER.stopScene();
						return;
					}

					WINDOW.update();
				}
				else if (!SCENEMANAGER.isActive())
				{
					TICKENGINE.stop();
				}

				RENDERENGINE.update();

				if (!flag)
				{
					WINDOW.poll();
				}
			}

			@Override
			public void onLastUpdate(TickEngine tickEngine)
			{
				RENDERENGINE.stop();
				WINDOW.destroy();
			}
		});

		RENDERENGINE = new RenderEngine(WINDOW);
		SCENEMANAGER = new SceneManager(RENDERENGINE);

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

		TICKENGINE.run();
	}
}
