package org.bstone.input.device;

import org.bstone.input.InputEngine;
import org.bstone.input.device.event.InputEvent;
import org.bstone.input.device.event.InputEventListener;
import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Andy on 1/22/18.
 *
 * All methods should only be called on the same thread.
 */
public class Keyboard implements InputDevice
{
	public static final int TEXT_CHAR = 0;

	private final Window window;
	private final InputEngine inputEngine;

	private final Queue<InputEventListener> listeners = new ArrayDeque<>();

	private boolean charEnabled = false;

	public Keyboard(Window window)
	{
		this(window, null);
	}

	/**
	 * Register this to the window (must be created).
	 *
	 * @param window the window to listen for input
	 * @param inputEngine to handle input events, can be null
	 */
	public Keyboard(Window window, InputEngine inputEngine)
	{
		this.window = window;
		this.inputEngine = inputEngine;
		this.inputEngine.setKeyboard(this);

		GLFWKeyCallbackI keyCallback = null;
		GLFWCharCallbackI charCallback = null;

		try
		{
			keyCallback = GLFW.glfwSetKeyCallback(this.window.handle(), this::onKey);
			charCallback = GLFW.glfwSetCharCallback(this.window.handle(), this::onCharText);

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

	public Keyboard addEventListener(InputEventListener listener)
	{
		this.listeners.add(listener);
		return this;
	}

	public Keyboard removeEventListener(InputEventListener listener)
	{
		this.listeners.remove(listener);
		return this;
	}

	public void clearEventListeners()
	{
		this.listeners.clear();
	}

	public void destroy()
	{
		this.clearEventListeners();

		GLFW.glfwSetKeyCallback(this.window.handle(), null);
		GLFW.glfwSetCharCallback(this.window.handle(), null);
	}

	@Override
	public void fireEvent(InputEvent event)
	{
		for(InputEventListener listener : this.listeners)
		{
			if (listener.onInputEvent(event))
			{
				return;
			}
		}

		if (this.inputEngine != null) this.inputEngine.addToEventQueue(event);
	}

	protected void onKey(long handle, int key, int scancode, int action, int mods)
	{
		if (action == GLFW.GLFW_RELEASE)
		{
			this.fireEvent(InputEvent.getButtonReleaseEvent(this, key));
		}
		else if (action == GLFW.GLFW_PRESS)
		{
			this.fireEvent(InputEvent.getButtonPressEvent(this, key));
		}
		else
		{
			//Ignore repeating input...
		}
	}

	protected void onCharText(long handle, int codepoint)
	{
		if (this.charEnabled)
		{
			this.fireEvent(InputEvent.getTextEvent(this, TEXT_CHAR, codepoint));
		}
	}

	public Keyboard setCharTextEnabled(boolean enabled)
	{
		this.charEnabled = enabled;
		return this;
	}

	public boolean isCharEnabled()
	{
		return this.charEnabled;
	}

	public Window getWindow()
	{
		return this.window;
	}

	public InputEngine getInputEngine()
	{
		return this.inputEngine;
	}

	@Override
	public String getName()
	{
		return "keyboard";
	}
}
