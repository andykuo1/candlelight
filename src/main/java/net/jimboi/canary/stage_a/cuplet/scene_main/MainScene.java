package net.jimboi.canary.stage_a.cuplet.scene_main;

import net.jimboi.canary.stage_a.cuplet.Cuplet;

import org.bstone.input.InputEngine;
import org.bstone.input.adapter.AxisAdapter;
import org.bstone.input.adapter.ButtonDownAdapter;
import org.bstone.input.adapter.ButtonPressAdapter;
import org.bstone.input.adapter.ButtonReleaseAdapter;
import org.bstone.input.device.Mouse;
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
		final InputEngine input = Cuplet.getCuplet().getInputEngine();

		input.registerInput("main", "mousex", new AxisAdapter(input.getMouse().getAxis(Mouse.AXIS_CURSORX)));
		input.registerInput("main", "mousey", new AxisAdapter(input.getMouse().getAxis(Mouse.AXIS_CURSORY)));

		input.registerInput("main", "fireleft", new ButtonPressAdapter(input.getMouse().getButton(Mouse.BUTTON_MOUSE_LEFT)));
		input.registerInput("main", "fireright", new ButtonPressAdapter(input.getMouse().getButton(Mouse.BUTTON_MOUSE_RIGHT)));
		input.registerInput("main", "mousescroll", new AxisAdapter(input.getMouse().getAxis(Mouse.AXIS_SCROLLY)));

		input.registerInput("main", "left",
				new ButtonDownAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_A)),
				new ButtonDownAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT)));
		input.registerInput("main", "right",
				new ButtonDownAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_D)),
				new ButtonDownAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_RIGHT)));
		input.registerInput("main", "up",
				new ButtonDownAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_W)),
				new ButtonDownAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_UP)));
		input.registerInput("main", "down",
				new ButtonDownAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_S)),
				new ButtonDownAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_DOWN)));

		input.registerInput("main", "rollLeft",
				new ButtonPressAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_A)),
				new ButtonPressAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT)));
		input.registerInput("main", "rollRight",
				new ButtonPressAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_D)),
				new ButtonPressAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_RIGHT)));
		input.registerInput("main", "rollUp",
				new ButtonPressAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_W)),
				new ButtonPressAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_UP)));
		input.registerInput("main", "rollDown",
				new ButtonPressAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_S)),
				new ButtonPressAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_DOWN)));

		input.registerInput("main", "action", new ButtonReleaseAdapter(input.getKeyboard().getButton(GLFW.GLFW_KEY_F)));
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
