package org.bstone.input;

import org.joml.Vector2dc;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 5/1/17.
 */
public class Mouse
{
	private static InputManager input;

	public static void initialize(InputManager inputManager)
	{
		input = inputManager;
	}

	public static boolean isLeftMouseDown()
	{
		return input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT);
	}

	public static boolean isLeftMousePressed()
	{
		return input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
	}

	public static boolean isLeftMouseReleased()
	{
		return input.isMouseReleased(GLFW.GLFW_MOUSE_BUTTON_LEFT);
	}

	public static boolean isRightMouseDown()
	{
		return input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
	}

	public static boolean isRightMousePressed()
	{
		return input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
	}

	public static boolean isRightMouseReleased()
	{
		return input.isMouseReleased(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
	}

	public static boolean isMiddleMouseDown()
	{
		return input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
	}

	public static boolean isMiddleMousePressed()
	{
		return input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
	}

	public static boolean isMiddleMouseReleased()
	{
		return input.isMouseReleased(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
	}

	public static Vector2dc getMousePosition()
	{
		return input.getMousePosition();
	}

	public static Vector2dc getMouseMotion()
	{
		return input.getMouseMotion();
	}

	public static Vector2dc getMouseScroll()
	{
		return input.getMouseScroll();
	}

	public static InputManager getInputManager()
	{
		return input;
	}
}
