package net.jimboi.base;

import net.jimboi.dood.SceneDood;
import net.jimboi.mod.renderer.RenderEngine;

import org.bstone.input.InputEngine;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;
import org.qsilver.scene.Scene;
import org.qsilver.scene.SceneManager;

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

	public static void main(String[] args)
	{
		SCENEMANAGER = new SceneManager();

		RENDERENGINE = new RenderEngine();

		TICKENGINE = new TickEngine();
		TICKENGINE.onFixedUpdate.addListener(SCENEMANAGER::update);
		TICKENGINE.onUpdate.addListener(() ->
		{
			SCENEMANAGER.render();
			RENDERENGINE.update();
		});

		WINDOW = new Window("Application", 640, 480);
		INPUTENGINE = WINDOW.getInputEngine();

		SCENEMANAGER.nextScene(SCENE = new SceneDood());

		RENDERENGINE.start();

		while(TICKENGINE.shouldKeepRunning())
		{
			if (WINDOW.shouldCloseWindow())
			{
				TICKENGINE.stop();
				continue;
			}

			WINDOW.update();
			TICKENGINE.update();
			WINDOW.poll();
		}
		SCENEMANAGER.clear();

		RENDERENGINE.stop();
		WINDOW.destroy();
	}
}
