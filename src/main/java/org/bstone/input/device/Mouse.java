package org.bstone.input.device;

import org.bstone.input.InputEngine;
import org.bstone.input.device.event.InputEvent;
import org.bstone.input.device.event.InputEventListener;
import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Andy on 1/22/18.
 *
 * All methods should only be called on the same thread.
 */
public class Mouse implements InputDevice
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
	private final InputEngine inputEngine;

	private final Queue<InputEventListener> listeners = new ArrayDeque<>();

	private boolean locked;

	public Mouse(Window window)
	{
		this(window, null);
	}

	/**
	 * Register this to the window (must be created).
	 *
	 * @param window the window to listen for input
	 * @param inputEngine to handle input events, can be null
	 */
	public Mouse(Window window, InputEngine inputEngine)
	{
		this.window = window;
		this.inputEngine = inputEngine;
		this.inputEngine.setMouse(this);

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

	public Mouse addEventListener(InputEventListener listener)
	{
		this.listeners.add(listener);
		return this;
	}

	public Mouse removeEventListener(InputEventListener listener)
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

		GLFW.glfwSetMouseButtonCallback(this.window.handle(), null);
		GLFW.glfwSetScrollCallback(this.window.handle(), null);
		GLFW.glfwSetCursorPosCallback(this.window.handle(), null);
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

	protected void onMouseButton(long handle, int button, int action, int mods)
	{
		if (action == GLFW.GLFW_RELEASE)
		{
			this.fireEvent(InputEvent.getButtonReleaseEvent(this, button));
		}
		else if (action == GLFW.GLFW_PRESS)
		{
			this.fireEvent(InputEvent.getButtonPressEvent(this, button));
		}
		else
		{
			//Ignore repeating input...
		}
	}

	protected void onMouseScroll(long handle, double xoffset, double yoffset)
	{
		this.fireEvent(InputEvent.getAxisEvent(this, AXIS_SCROLLX, (float) xoffset));
		this.fireEvent(InputEvent.getAxisEvent(this, AXIS_SCROLLY, (float) yoffset));
	}

	protected void onMousePosition(long handle, double xpos, double ypos)
	{
		this.fireEvent(InputEvent.getAxisEvent(this, AXIS_CURSORX, (float) xpos));
		this.fireEvent(InputEvent.getAxisEvent(this, AXIS_CURSORY, (float) ypos));
	}

	public void setCursorMode(boolean locked)
	{
		if (this.locked == locked) return;
		this.locked = locked;

		GLFW.glfwSetInputMode(this.window.handle(), GLFW.GLFW_CURSOR, this.locked ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}

	public boolean getCursorMode()
	{
		return this.locked;
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
		return "mouse";
	}
}
