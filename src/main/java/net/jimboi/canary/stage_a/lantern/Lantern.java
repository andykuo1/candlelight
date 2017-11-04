package net.jimboi.canary.stage_a.lantern;

import net.jimboi.canary.stage_a.lantern.scene_main.SceneMain;
import net.jimboi.canary.stage_a.lantern.scene_main.SceneMainRenderer;
import net.jimboi.canary.stage_a.lantern.scene_test.SceneTest;
import net.jimboi.canary.stage_a.lantern.scene_test.SceneTestRenderer;

import org.bstone.application.Application;
import org.bstone.application.game.GameEngine;
import org.bstone.scene.SceneManager;
import org.bstone.tick.TickEngine;

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
	}

	private SceneManager sceneManager;

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{
		this.sceneManager = new SceneManager(this.getRenderEngine().getRenderServices());
		this.sceneManager.registerScene("test", SceneTest.class, SceneTestRenderer.class);
		this.sceneManager.registerScene("init", SceneMain.class, SceneMainRenderer.class);

		this.sceneManager.setNextScene("init");
	}

	@Override
	public void onFixedUpdate()
	{
		this.sceneManager.update();
	}

	@Override
	public void onLastUpdate(TickEngine tickEngine)
	{
		this.sceneManager.destroy();
	}

	public final SceneManager getSceneManager()
	{
		return this.sceneManager;
	}

	public final Application getApplication()
	{
		return application;
	}
}
