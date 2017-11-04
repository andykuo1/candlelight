package net.jimboi.canary.stage_a.cuplet;

import net.jimboi.canary.stage_a.cuplet.scene_main.MainRenderer;
import net.jimboi.canary.stage_a.cuplet.scene_main.MainScene;

import org.bstone.application.Application;
import org.bstone.application.game.GameEngine;
import org.bstone.input.InputContext;
import org.bstone.input.InputEngine;
import org.bstone.input.InputListener;
import org.bstone.scene.SceneManager;
import org.bstone.tick.TickEngine;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 10/29/17.
 */
public class Cuplet extends GameEngine implements InputListener
{
	private static Cuplet instance;
	private static Application application;

	public static Cuplet getCuplet()
	{
		return instance;
	}

	public static void main(String[] args)
	{
		application = new Application()
				.setFramework(instance = new Cuplet());
		application.run();
	}

	protected static boolean DEBUG;

	public static boolean isDebugMode()
	{
		return DEBUG;
	}

	private SceneManager sceneManager;

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{
		this.sceneManager = new SceneManager(this.getRenderEngine().getRenderServices());
		this.sceneManager.registerScene("init", MainScene.class, MainRenderer.class);

		final InputEngine input = this.getInputEngine();
		input.getDefaultContext().registerEvent("_debug", input.getKeyboard().getButton(GLFW.GLFW_KEY_P)::getAction);
		input.getDefaultContext().addListener(0, this);

		this.sceneManager.setNextScene("init");
	}

	@Override
	public void onInputUpdate(InputContext context)
	{
		final InputEngine input = this.getInputEngine();
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
	public void onLastUpdate(TickEngine tickEngine)
	{
		this.sceneManager.destroy();

		this.getInputEngine().getDefaultContext().removeListener(this);
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
