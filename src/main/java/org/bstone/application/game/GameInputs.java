package org.bstone.application.game;

import org.bstone.input.InputContext;
import org.bstone.input.InputEngine;
import org.bstone.input.adapter.InputAdapter;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 12/2/17.
 */
public class GameInputs
{
	public static void loadBaseInputs(InputEngine inputs)
	{
		final InputContext context = inputs.getDefaultContext();

		context.registerEvent("mousex", inputs.getMouse().getCursorX()::getRange);
		context.registerEvent("mousey", inputs.getMouse().getCursorY()::getRange);

		context.registerEvent("mouseleft", inputs.getMouse().getButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)::getAction);
		context.registerEvent("mouseright", inputs.getMouse().getButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT)::getAction);

		context.registerEvent("mouselock", inputs.getKeyboard().getButton(GLFW.GLFW_KEY_P)::getAction);

		context.registerEvent("exit", inputs.getKeyboard().getButton(GLFW.GLFW_KEY_ESCAPE)::getState);

		context.registerEvent("forward",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_W)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_S)::getState
				),
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_UP)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_DOWN)::getState
				));

		context.registerEvent("up",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_SPACE)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_E)::getState
				));

		context.registerEvent("right",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_D)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_A)::getState
				),
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_RIGHT)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT)::getState
				));

		context.registerEvent("sprint",
				inputs.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT_SHIFT)::getState);

		context.registerEvent("action",
				inputs.getKeyboard().getButton(GLFW.GLFW_KEY_F)::getState);
	}
}
