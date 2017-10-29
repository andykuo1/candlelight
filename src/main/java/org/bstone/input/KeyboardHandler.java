package org.bstone.input;

import org.bstone.input.mapping.ButtonInput;
import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Created by Andy on 10/12/17.
 */
public class KeyboardHandler extends InputHandler
{
	protected final Window window;

	KeyboardHandler(Window window)
	{
		this.window = window;

		GLFWKeyCallbackI keyCallback = null;

		try
		{
			keyCallback = GLFW.glfwSetKeyCallback(this.window.handle(),
					(handle, key, scancode, action, mods) ->
					{
						ButtonInput input = this.getButton(key);
						if (input != null)
						{
							if (action == GLFW.GLFW_RELEASE)
							{
								input.release();
							}
							else if (action == GLFW.GLFW_PRESS)
							{
								input.press();
							}
							else
							{
								//Ignore repeating input...
							}
						}
					});

			if (keyCallback != null)
			{
				throw new IllegalStateException("another keyboard handler is already registered!");
			}
		}
		catch (Exception e)
		{
			//Reset to previous state
			GLFW.glfwSetKeyCallback(this.window.handle(), keyCallback);

			throw e;
		}
	}

	@Override
	protected boolean isRawButtonID(int id)
	{
		return id >= 0 && id <= GLFW.GLFW_KEY_LAST;
	}

	@Override
	protected boolean isRawAxisID(int id)
	{
		return false;
	}
}
