package net.jimboi.canary.stage_a.cuplet;

import net.jimboi.canary.stage_a.cuplet.scene_main.MainRenderer;
import net.jimboi.canary.stage_a.cuplet.scene_main.MainScene;

import org.bstone.application.Application;
import org.bstone.application.game.Game;
import org.bstone.application.game.GameEngine;
import org.bstone.input.InputContext;
import org.bstone.input.InputEngine;
import org.bstone.input.InputListener;
import org.bstone.scene.SceneManager;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 10/29/17.
 */
public class Cuplet implements Game, InputListener
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

	protected static boolean DEBUG;

	public static boolean isDebugMode()
	{
		return DEBUG;
	}

	private SceneManager sceneManager;

	public Cuplet()
	{
	}

	@Override
	public void onFirstUpdate()
	{
		this.sceneManager = new SceneManager(this.getFramework().getRenderEngine().getRenderServices());
		this.sceneManager.registerScene("init", MainScene.class, MainRenderer.class);

		final InputEngine input = this.getFramework().getInputEngine();
		input.getDefaultContext().registerEvent("_debug", input.getKeyboard().getButton(GLFW.GLFW_KEY_P)::getAction);
		input.getDefaultContext().addListener(0, this);

		this.sceneManager.setNextScene("init");
	}

	@Override
	public void onInputUpdate(InputContext context)
	{
		final InputEngine input = this.getFramework().getInputEngine();
		if (input.getDefaultContext().getAction("_debug").isPressedAndConsume())
		{
			DEBUG = !DEBUG;
		}
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

		Cuplet.getCuplet().getFramework().getInputEngine().getDefaultContext().removeListener(this);
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
