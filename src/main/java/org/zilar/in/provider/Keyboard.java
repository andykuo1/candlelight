package org.zilar.in.provider;

import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.zilar.in.InputState;
import org.zilar.in.handler.CharHandler;
import org.zilar.in.handler.HandlerList;

/**
 * Created by Andy on 1/22/18.
 */
public class Keyboard implements InputProvider
{
	private final Window window;
	private final InputState inputState;

	private final HandlerList<CharHandler> charHandlers = new HandlerList<>();
	private final StringBuffer charBuffer = new StringBuffer();
	private boolean enabledChar = false;

	public Keyboard(Window window)
	{
		this.window = window;

		this.inputState = new InputState(this, 0, GLFW.GLFW_KEY_LAST + 1);

		GLFWKeyCallbackI keyCallback = null;
		GLFWCharCallbackI charCallback = null;

		try
		{
			keyCallback = GLFW.glfwSetKeyCallback(this.window.handle(), this::onKey);
			charCallback = GLFW.glfwSetCharCallback(this.window.handle(), this::onChar);

			if (keyCallback != null)
			{
				throw new IllegalStateException("another keyboard handler is already registered to this window!");
			}

			if (charCallback != null)
			{
				throw new IllegalStateException("another text handler is already registered to this window!");
			}
		}
		catch (Exception e)
		{
			//Reset to previous state
			GLFW.glfwSetKeyCallback(this.window.handle(), keyCallback);
			GLFW.glfwSetCharCallback(this.window.handle(), charCallback);

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

	protected void onChar(long handle, int codepoint)
	{
		if (this.enabledChar && this.onCharUpdate(this.charBuffer, codepoint))
		{
			this.charBuffer.append((char) codepoint);
		}
	}

	protected boolean onCharUpdate(StringBuffer buffer, int codepoint)
	{
		for(CharHandler handler : this.charHandlers)
		{
			if (handler.onCharUpdate(buffer, codepoint))
			{
				return false;
			}
		}
		return true;
	}

	public Keyboard setCharEnabled(boolean enabled)
	{
		this.enabledChar = enabled;
		return this;
	}

	public boolean isCharEnabled()
	{
		return this.enabledChar;
	}

	public StringBuffer getCharBuffer()
	{
		return this.charBuffer;
	}

	public void clearCharBuffer()
	{
		this.charBuffer.setLength(0);
	}

	public HandlerList<CharHandler> getCharHandlers()
	{
		return this.charHandlers;
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
