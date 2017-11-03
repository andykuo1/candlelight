package net.jimboi.canary.stage_a.cuplet.scene_main;

import net.jimboi.canary.stage_a.cuplet.Cuplet;

import org.bstone.input.InputContext;
import org.bstone.input.InputEngine;
import org.bstone.scene.Scene;
import org.bstone.scene.SceneManager;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 10/29/17.
 */
public class MainScene extends Scene
{
	private GobletWorld world;
	private boolean init = true;

	public MainScene()
	{
		this.world = new GobletWorld();
	}

	@Override
	protected void onSceneCreate(SceneManager sceneManager)
	{
		final InputEngine input = Cuplet.getCuplet().getFramework().getInputEngine();
		final InputContext ctx = input.getDefaultContext();
		ctx.registerEvent("mousex",
				input.getMouse().getCursorX()::getRange);
		ctx.registerEvent("mousey",
				input.getMouse().getCursorY()::getRange);
		ctx.registerEvent("mouseleft",
				input.getMouse().getButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)::getState);
		ctx.registerEvent("mouseright",
				input.getMouse().getButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT)::getState);
		ctx.registerEvent("mousescroll",
				input.getMouse().getScrollY()::getRange);
		ctx.registerEvent("left",
				input.getKeyboard().getButton(GLFW.GLFW_KEY_A)::getState,
				input.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT)::getState);
		ctx.registerEvent("right",
				input.getKeyboard().getButton(GLFW.GLFW_KEY_D)::getState,
				input.getKeyboard().getButton(GLFW.GLFW_KEY_RIGHT)::getState);
		ctx.registerEvent("up",
				input.getKeyboard().getButton(GLFW.GLFW_KEY_W)::getState,
				input.getKeyboard().getButton(GLFW.GLFW_KEY_UP)::getState);
		ctx.registerEvent("down",
				input.getKeyboard().getButton(GLFW.GLFW_KEY_S)::getState,
				input.getKeyboard().getButton(GLFW.GLFW_KEY_DOWN)::getState);
		ctx.registerEvent("action",
				input.getKeyboard().getButton(GLFW.GLFW_KEY_F)::getState);
	}

	@Override
	protected void onSceneUpdate()
	{
		if (this.init)
		{
			this.world.start();
			this.init = false;
		}
		else
		{
			this.world.update();
		}
	}

	@Override
	protected void onSceneDestroy()
	{
		this.world.stop();
	}

	public GobletWorld getWorld()
	{
		return this.world;
	}
}
