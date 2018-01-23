package org.zilar.in;

import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

/**
 * Created by Andy on 1/22/18.
 */
public class Mouse implements InputProvider
{
	public static final int AXIS_CURSORX = 0;
	public static final int AXIS_CURSORY = 1;
	public static final int AXIS_SCROLLX = 2;
	public static final int AXIS_SCROLLY = 3;
	public static final int AXIS_LAST = AXIS_SCROLLY;

	public static final int BUTTON_MOUSE_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int BUTTON_MOUSE_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	public static final int BUTTON_MOUSE_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	public static final int BUTTON_LAST = GLFW.GLFW_MOUSE_BUTTON_LAST;

	private final Window window;
	private final InputState inputState = new InputState(AXIS_LAST + 1, BUTTON_LAST + 1);

	public Mouse(Window window)
	{
		this.window = window;

		GLFWMouseButtonCallbackI mouseButtonCallback = null;
		GLFWScrollCallbackI scrollCallback = null;
		GLFWCursorPosCallbackI cursorPosCallback = null;

		try
		{
			mouseButtonCallback = GLFW.glfwSetMouseButtonCallback(this.window.handle(), this::onMouseButton);
			scrollCallback = GLFW.glfwSetScrollCallback(this.window.handle(), this::onMouseScroll);
			cursorPosCallback = GLFW.glfwSetCursorPosCallback(this.window.handle(), this::onMousePosition);

			if (mouseButtonCallback != null)
			{
				throw new IllegalStateException("another mouse button handler is already registered to this window!");
			}

			if (scrollCallback != null)
			{
				throw new IllegalStateException("another mouse scroll handler is already registered to this window!");
			}

			if (cursorPosCallback != null)
			{
				throw new IllegalStateException("another mouse position handler is already registered to this window!");
			}
		}
		catch(Exception e)
		{
			//Reset to previous state
			GLFW.glfwSetMouseButtonCallback(this.window.handle(), mouseButtonCallback);
			GLFW.glfwSetScrollCallback(this.window.handle(), scrollCallback);
			GLFW.glfwSetCursorPosCallback(this.window.handle(), cursorPosCallback);

			throw e;
		}
	}

	protected void onMouseButton(long handle, int button, int action, int mods)
	{
		if (action == GLFW.GLFW_RELEASE)
		{
			this.inputState.releaseButton(button);
		}
		else if (action == GLFW.GLFW_PRESS)
		{
			this.inputState.pressButton(button);
		}
		else
		{
			//Ignore repeating input...
		}
	}

	protected void onMouseScroll(long handle, double xoffset, double yoffset)
	{
		this.inputState.moveAxis(AXIS_SCROLLX, (float) xoffset);
		this.inputState.moveAxis(AXIS_SCROLLY, (float) xoffset);
	}

	protected void onMousePosition(long handle, double xpos, double ypos)
	{
		this.inputState.setAxis(AXIS_CURSORX, (float) xpos);
		this.inputState.setAxis(AXIS_CURSORY, (float) ypos);
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
