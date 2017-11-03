package net.jimboi.canary.stage_a.lantern;

import net.jimboi.canary.stage_a.lantern.scene_main.LanternSceneMain;
import net.jimboi.canary.stage_a.lantern.scene_main.RenderSceneMain;

import org.bstone.application.Application;
import org.bstone.application.game.Game;
import org.bstone.application.game.GameEngine;
import org.bstone.scene.SceneManager;
import org.zilar.resource.ResourceLocation;

/**
 * Created by Andy on 10/20/17.
 */
public class Lantern implements Game
{
	private static Lantern instance;
	private static GameEngine framework;
	private static Application application;

	public static Lantern getLantern()
	{
		return instance;
	}

	public static void main(String[] args)
	{
		instance = new Lantern();
		framework = new GameEngine(instance);
		application = new Application(framework);
		application.run();
	}

	private SceneManager sceneManager;

	public Lantern()
	{
	}

	@Override
	public void onFirstUpdate()
	{
		this.sceneManager = new SceneManager(this.getFramework().getRenderEngine().getRenderServices());
		this.sceneManager.registerScene("init", LanternSceneMain.class, RenderSceneMain.class);
		this.sceneManager.setNextScene("init");
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

	@Override
	public ResourceLocation getAssetLocation()
	{
		return new ResourceLocation("lantern:lantern.assets");
	}
}
