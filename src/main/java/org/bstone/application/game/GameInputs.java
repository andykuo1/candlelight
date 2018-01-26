package org.bstone.application.game;

import org.bstone.input.InputEngine;
import org.bstone.input.adapter.AxisAdapter;
import org.bstone.input.adapter.AxisMotionAdapter;
import org.bstone.input.adapter.ButtonDownAdapter;
import org.bstone.input.adapter.ButtonPressAdapter;
import org.bstone.input.adapter.ButtonReleaseAdapter;
import org.bstone.input.adapter.VirtualAxisAdapter;
import org.bstone.input.device.Keyboard;
import org.bstone.input.device.Mouse;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 12/2/17.
 */
public class GameInputs
{
	public static void loadBaseInputs(InputEngine inputs)
	{
		final Mouse mouse = inputs.getMouse();
		final Keyboard keyboard = inputs.getKeyboard();

		inputs.registerInput("main", "mousex", new AxisAdapter(mouse.getAxis(Mouse.AXIS_CURSORX)));
		inputs.registerInput("main", "mousey", new AxisAdapter(mouse.getAxis(Mouse.AXIS_CURSORY)));

		inputs.registerInput("main", "lookx", new AxisMotionAdapter(mouse.getAxis(Mouse.AXIS_CURSORX)));
		inputs.registerInput("main", "looky", new AxisMotionAdapter(mouse.getAxis(Mouse.AXIS_CURSORY)));

		inputs.registerInput("main", "mouseleft", new ButtonReleaseAdapter(mouse.getButton(Mouse.BUTTON_MOUSE_LEFT)));
		inputs.registerInput("main", "mouseright", new ButtonReleaseAdapter(mouse.getButton(Mouse.BUTTON_MOUSE_RIGHT)));

		inputs.registerInput("main", "fireleft", new ButtonPressAdapter(mouse.getButton(Mouse.BUTTON_MOUSE_LEFT)));
		inputs.registerInput("main", "fireright", new ButtonPressAdapter(mouse.getButton(Mouse.BUTTON_MOUSE_RIGHT)));

		inputs.registerInput("main", "mouselock", new ButtonReleaseAdapter(keyboard.getButton(GLFW.GLFW_KEY_P)));
		inputs.registerInput("main", "exit", new ButtonReleaseAdapter(keyboard.getButton(GLFW.GLFW_KEY_ESCAPE)));

		inputs.registerInput("main", "forward",
				new VirtualAxisAdapter(keyboard.getButton(GLFW.GLFW_KEY_W),
						keyboard.getButton(GLFW.GLFW_KEY_S)),
				new VirtualAxisAdapter(keyboard.getButton(GLFW.GLFW_KEY_UP),
						keyboard.getButton(GLFW.GLFW_KEY_DOWN)));

		inputs.registerInput("main", "up",
				new VirtualAxisAdapter(keyboard.getButton(GLFW.GLFW_KEY_SPACE),
						keyboard.getButton(GLFW.GLFW_KEY_E)));

		inputs.registerInput("main", "right",
				new VirtualAxisAdapter(keyboard.getButton(GLFW.GLFW_KEY_D),
						keyboard.getButton(GLFW.GLFW_KEY_A)),
				new VirtualAxisAdapter(keyboard.getButton(GLFW.GLFW_KEY_RIGHT),
						keyboard.getButton(GLFW.GLFW_KEY_LEFT)));

		inputs.registerInput("main", "sprint", new ButtonDownAdapter(keyboard.getButton(GLFW.GLFW_KEY_LEFT_SHIFT)));
		inputs.registerInput("main", "action", new ButtonReleaseAdapter(keyboard.getButton(GLFW.GLFW_KEY_F)));
	}
}
