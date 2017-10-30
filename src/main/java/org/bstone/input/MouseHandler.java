package org.bstone.input;

import org.bstone.input.context.raw.AxisInput;
import org.bstone.input.context.raw.ButtonInput;
import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

/**
 * Created by Andy on 10/13/17.
 */
public class MouseHandler extends InputHandler
{
	public static final int AXIS_CURSOR_X = 0;
	public static final int AXIS_CURSOR_Y = 1;
	public static final int AXIS_SCROLL_X = 2;
	public static final int AXIS_SCROLL_Y = 3;

	protected final Window window;

	private boolean locked;

	MouseHandler(Window window)
	{
		this.window = window;

		GLFWMouseButtonCallbackI mouseButtonCallback = null;
		GLFWScrollCallbackI scrollCallback = null;
		GLFWCursorPosCallbackI cursorPosCallback = null;

		try
		{
			mouseButtonCallback = GLFW.glfwSetMouseButtonCallback(this.window.handle(),
					(handle, button, action, mods) ->
					{
						ButtonInput input = this.getButton(button);
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

			scrollCallback = GLFW.glfwSetScrollCallback(this.window.handle(),
					(handle, xoffset, yoffset) ->
					{
						AxisInput inputx = this.getAxis(AXIS_SCROLL_X);
						if (inputx != null)
						{
							inputx.move((float) xoffset);
						}

						AxisInput inputy = this.getAxis(AXIS_SCROLL_Y);
						if (inputy != null)
						{
							inputy.move((float) yoffset);
						}
					});

			cursorPosCallback = GLFW.glfwSetCursorPosCallback(this.window.handle(),
					(handle, xpos, ypos) ->
					{
						AxisInput mousex = this.getAxis(AXIS_CURSOR_X);
						if (mousex != null)
						{
							mousex.set((float) xpos);
						}

						AxisInput mousey = this.getAxis(AXIS_CURSOR_Y);
						if (mousey != null)
						{
							mousey.set((float) ypos);
						}
					});


			if (mouseButtonCallback != null)
			{
				throw new IllegalStateException("another mouse button handler is already registered!");
			}

			if (scrollCallback != null)
			{
				throw new IllegalStateException("another mouse scroll handler is already registered!");
			}

			if (cursorPosCallback != null)
			{
				throw new IllegalStateException("another mouse position handler is already registered!");
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

	public final AxisInput getCursorX()
	{
		return this.getAxis(AXIS_CURSOR_X);
	}

	public final AxisInput getCursorY()
	{
		return this.getAxis(AXIS_CURSOR_Y);
	}

	public final AxisInput getScrollX()
	{
		return this.getAxis(AXIS_SCROLL_X);
	}

	public final AxisInput getScrollY()
	{
		return this.getAxis(AXIS_SCROLL_Y);
	}

	public void setCursorMode(boolean locked)
	{
		//TODO: this needs to be called on the input thread, NOT THE MAIN!
		if (this.locked == locked) return;

		this.locked = locked;

		GLFW.glfwSetInputMode(this.window.handle(), GLFW.GLFW_CURSOR, this.locked ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}

	public boolean getCursorMode()
	{
		return this.locked;
	}

	@Override
	protected boolean isButtonID(int id)
	{
		return id >= 0 && id < 8;
	}

	@Override
	protected boolean isAxisID(int id)
	{
		return id >= 0 && id < 4;
	}
}
