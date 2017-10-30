package net.jimboi.canary.stage_a.cuplet;

import org.bstone.application.Application;
import org.bstone.application.game.Game;
import org.bstone.application.game.GameEngine;
import org.bstone.scene.SceneManager;

/**
 * Created by Andy on 10/29/17.
 */
public class Cuplet implements Game
{
	private static Cuplet instance;
	private static GameEngine framework;
	private static Application application;

	public static Cuplet getCuplet()
	{
		return instance;
	}

	public static void main(String[] args)
	{
		instance = new Cuplet();
		framework = new GameEngine(instance);
		application = new Application(framework);
		application.run();
	}

	private final SceneManager sceneManager;

	public Cuplet()
	{
		this.sceneManager = new SceneManager();

		this.sceneManager.registerScene("init", net.jimboi.canary.stage_a.cuplet.scene_main.MainScene.class);
	}

	@Override
	public void onFirstUpdate()
	{
		this.sceneManager.setNextScene("init", scene -> {
			this.getFramework().getRenderEngine().getRenderServices().startService(
					new net.jimboi.canary.stage_a.cuplet.scene_main.MainRenderer(this.getFramework().getRenderEngine(), scene));
		});
	}

	@Override
	public void onFixedUpdate()
	{
		this.sceneManager.update();
	}

	@Override
	public void onLastUpdate()
	{
		this.sceneManager.destroy();
	}

	public final SceneManager getSceneManager()
	{
		return this.sceneManager;
	}

	public final GameEngine getFramework()
	{
		return framework;
	}

	public final Application getApplication()
	{
		return application;
	}
}
