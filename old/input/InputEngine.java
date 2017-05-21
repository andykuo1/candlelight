package org.bstone.input;

import org.bstone.window.Window;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 4/26/17.
 */
public class InputEngine
{
	private final boolean[] mouseaction = new boolean[3];
	private final boolean[] keyaction = new boolean[512];

	private final Vector2d mousepos = new Vector2d();
	private final Vector2d mouseprev = new Vector2d();
	private final Vector2d mousemotion = new Vector2d();
	private final Vector2d mousescroll = new Vector2d();
	private boolean mouselocked = false;

	private final Window window;

	public InputEngine(Window window)
	{
		this.window = window;

		GLFW.glfwSetKeyCallback(window.handle(), (handle, key, scancode, action, mods) ->
		{
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
			{
				GLFW.glfwSetWindowShouldClose(handle, true);
			}
			else
			{
				this.updateKey(key, action);
			}
		});

		GLFW.glfwSetMouseButtonCallback(window.handle(), (handle, button, action, mods)->
		{
			this.updateMouse(button, action);
		});

		GLFW.glfwSetCursorPosCallback(window.handle(), (handle, xpos, ypos) ->
		{
			this.mousepos.set(xpos, ypos);
		});

		GLFW.glfwSetScrollCallback(window.handle(), (handle, xoffset, yoffset)->{
			this.mousescroll.add(xoffset, yoffset);
		});
	}

	public void poll()
	{
		this.mousemotion.set(this.mouseprev);
		this.mousemotion.sub(this.mousepos);
		this.mouseprev.set(this.mousepos);

		/*
		if (this.mouselocked)
		{
			System.out.println(this.mousemotion);
			GLFW.glfwSetCursorPos(this.window.handle(), 100, 100);
		}
		*/

		GLFW.glfwPollEvents();
	}

	public void updateKey(int key, int action)
	{
		if (key >= this.keyaction.length || key < 0) return;

		if (action == GLFW.GLFW_PRESS)
		{
			this.keyaction[key] = true;
		}
		else if (action == GLFW.GLFW_RELEASE)
		{
			this.keyaction[key] = false;
		}
	}

	public void updateMouse(int button, int action)
	{
		if (button >= this.mouseaction.length || button < 0) return;

		if (action == GLFW.GLFW_PRESS)
		{
			this.mouseaction[button] = true;
		}
		else if (action == GLFW.GLFW_RELEASE)
		{
			this.mouseaction[button] = false;
		}
	}

	public void setCursorMode(boolean locked)
	{
		this.mouselocked = locked;
		GLFW.glfwSetInputMode(this.window.handle(), GLFW.GLFW_CURSOR, this.mouselocked ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}

	public boolean getCursorMode()
	{
		return this.mouselocked;
	}

	public Vector2dc getMousePosition()
	{
		return this.mousepos;
	}

	public Vector2dc getMouseMotion()
	{
		return this.mousemotion;
	}

	public Vector2dc getMouseScroll()
	{
		return this.mousescroll;
	}

	public boolean isKeyDown(int keycode)
	{
		return this.keyaction[keycode];
	}

	public boolean isMouseDown(int button)
	{
		return this.mouseaction[button];
	}

	public Window getWindow()
	{
		return this.window;
	}
}
