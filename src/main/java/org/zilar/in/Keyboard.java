package org.zilar.in;

import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Created by Andy on 1/22/18.
 */
public class Keyboard implements InputProvider
{
	private final Window window;
	private final InputState inputState = new InputState(0, GLFW.GLFW_KEY_LAST + 1);

	public Keyboard(Window window)
	{
		this.window = window;

		GLFWKeyCallbackI keyCallback = null;

		try
		{
			keyCallback = GLFW.glfwSetKeyCallback(this.window.handle(), this::onKey);

			if (keyCallback != null)
			{
				throw new IllegalStateException("another keyboard handler is already registered to this window!");
			}
		}
		catch (Exception e)
		{
			//Reset to previous state
			GLFW.glfwSetKeyCallback(this.window.handle(), keyCallback);

			throw e;
		}
	}

	protected void onKey(long handle, int key, int scancode, int action, int mods)
	{
		if (action == GLFW.GLFW_RELEASE)
		{
			this.inputState.releaseButton(key);
		}
		else if (action == GLFW.GLFW_PRESS)
		{
			this.inputState.pressButton(key);
		}
		else
		{
			//Ignore repeating input...
		}
	}

	public InputState getInputState()
	{
		return this.inputState;
	}

	public Window getWindow()
	{
		return this.window;
	}
}
