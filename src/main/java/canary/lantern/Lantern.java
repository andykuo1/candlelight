package canary.lantern;

import canary.lantern.scene_main.SceneMain;
import canary.lantern.scene_main.SceneMainRenderer;
import canary.lantern.scene_test.SceneTest;
import canary.lantern.scene_test.SceneTestRenderer;

import canary.bstone.application.Application;
import canary.bstone.application.game.GameEngine;
import canary.bstone.tick.TickEngine;

/**
 * Created by Andy on 10/20/17.
 */
public class Lantern extends GameEngine
{
	private static Lantern instance;
	private static Application application;

	public static Lantern getLantern()
	{
		return instance;
	}

	public static void main(String[] args)
	{
		application = new Application()
				.setFramework(instance = new Lantern());
		application.run();
		System.exit(0);
	}

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{
		this.sceneManager.registerScene("canary/test", SceneTest.class, SceneTestRenderer.class);
		this.sceneManager.registerScene("init", SceneMain.class, SceneMainRenderer.class);

		this.sceneManager.setNextScene("init");
	}

	public final Application getApplication()
	{
		return application;
	}
}
