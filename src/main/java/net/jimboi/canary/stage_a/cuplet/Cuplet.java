package net.jimboi.canary.stage_a.cuplet;

import net.jimboi.canary.stage_a.cuplet.scene_main.MainRenderer;
import net.jimboi.canary.stage_a.cuplet.scene_main.MainScene;

import org.bstone.application.Application;
import org.bstone.application.game.GameEngine;
import org.bstone.input.InputEngine;
import org.bstone.input.adapter.ButtonReleaseAdapter;
import org.bstone.tick.TickEngine;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 10/29/17.
 */
public class Cuplet extends GameEngine
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
		System.exit(0);
	}

	protected static boolean DEBUG;

	public static boolean isDebugMode()
	{
		return DEBUG;
	}

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{
		this.sceneManager.registerScene("init", MainScene.class, MainRenderer.class);

		final InputEngine input = this.getInputEngine();
		input.registerInput("main", "debug", new ButtonReleaseAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_P)));

		this.sceneManager.setNextScene("init");
	}

	@Override
	public void onFixedUpdate()
	{
		super.onFixedUpdate();

		if (this.getInputEngine().getAction("debug"))
		{
			DEBUG = !DEBUG;
		}
	}

	public final Application getApplication()
	{
		return application;
	}
}
