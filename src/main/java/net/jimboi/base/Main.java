package net.jimboi.base;

import net.jimboi.mod3.blob.SceneBlob;

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

	public static SceneManager SCENEMANAGER;

	public static void main(String[] args)
	{
		TICKENGINE = new TickEngine(new TickEngine.Listener()
		{
			@Override
			public void onUpdate(double delta)
			{
				SCENEMANAGER.update(delta);
			}

			@Override
			public void onUpdate()
			{
				SCENEMANAGER.render();
			}
		});
		WINDOW = new Window("Application", 640, 480);

		INPUTENGINE = WINDOW.getInputEngine();

		SCENEMANAGER = new SceneManager();
		SCENEMANAGER.nextScene(SCENE = new SceneBlob());

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

		WINDOW.destroy();
	}
}
