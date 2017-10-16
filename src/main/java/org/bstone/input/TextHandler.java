package org.bstone.input;

import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;

/**
 * Created by Andy on 10/13/17.
 */
public class TextHandler
{
	protected final Window window;

	private final StringBuffer sb = new StringBuffer();

	public TextHandler(Window window)
	{
		this.window = window;

		GLFWCharCallbackI callback = null;

		try
		{
			callback = GLFW.glfwSetCharCallback(this.window.handle(),
					(handle, codepoint) ->
					{
						this.sb.append((char) codepoint);
					});

			if (callback != null)
			{
				throw new IllegalStateException("another text handler is already registered!");
			}
		}
		catch (Exception e)
		{
			//Reset to previous state
			GLFW.glfwSetCharCallback(this.window.handle(), callback);

			throw e;
		}
	}

	public StringBuffer getBuffer()
	{
		return this.sb;
	}

	public void clear()
	{
		this.sb.setLength(0);
	}
}
