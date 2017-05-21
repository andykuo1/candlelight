package org.bstone.input;

/**
 * Created by Andy on 5/1/17.
 */
public class Keyboard
{
	private static InputManager input;

	public static void initialize(InputManager inputManager)
	{
		input = inputManager;
	}

	public static boolean isKeyDown(String key)
	{
		return input.isKeyDown(key);
	}

	public static boolean isKeyPressed(String key)
	{
		return input.isKeyPressed(key);
	}

	public static boolean isKeyReleased(String key)
	{
		return input.isKeyReleased(key);
	}
}
