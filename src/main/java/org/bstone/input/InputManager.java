package org.bstone.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Andy on 5/20/17.
 */
public class InputManager
{
	protected static InputEngine INPUT_ENGINE;

	private static final Map<String, Input[]> inputMapping = new HashMap<>();

	private InputManager() {}

	static void init(InputEngine inputEngine)
	{
		INPUT_ENGINE = inputEngine;
	}

	public static Input[] getInput(String id)
	{
		if (!inputMapping.containsKey(id)) throw new NoSuchElementException();

		return inputMapping.get(id);
	}

	public static boolean isInputDown(String id)
	{
		if (!inputMapping.containsKey(id)) throw new NoSuchElementException();

		Input[] inputs = inputMapping.get(id);
		for(Input input : inputs)
		{
			if (input.isDown())
			{
				return true;
			}
		}

		return false;
	}

	public static boolean isInputPressed(String id)
	{
		if (!inputMapping.containsKey(id)) throw new NoSuchElementException();

		Input[] inputs = inputMapping.get(id);
		for(Input input : inputs)
		{
			if (input.isPressed())
			{
				return true;
			}
		}

		return false;
	}

	public static boolean isInputReleased(String id)
	{
		if (!inputMapping.containsKey(id)) throw new NoSuchElementException();

		Input[] inputs = inputMapping.get(id);
		for(Input input : inputs)
		{
			if (input.isReleased())
			{
				return true;
			}
		}

		return false;
	}

	public static float getInputAmount(String id)
	{
		if (!inputMapping.containsKey(id)) throw new NoSuchElementException();

		Input[] inputs = inputMapping.get(id);
		for(Input input : inputs)
		{
			float f = input.getAmount();
			if (f != 0) return f;
		}

		return 0;
	}

	public static float getInputMotion(String id)
	{
		Input[] inputs = inputMapping.get(id);
		for(Input input : inputs)
		{
			float f = input.getMotion();
			if (f != 0) return f;
		}

		return 0;
	}

	public static void registerKey(String id, int... keycode)
	{
		registerInput(INPUT_ENGINE.getKeyboard(), id, keycode);
	}

	public static void registerMouse(String id, int... button)
	{
		registerInput(INPUT_ENGINE.getMouse(), id, button);
	}

	public static void registerMousePosX(String id)
	{
		registerInput(INPUT_ENGINE.getMouse(), id, MouseHandler.POS_X);
	}

	public static void registerMousePosY(String id)
	{
		registerInput(INPUT_ENGINE.getMouse(), id, MouseHandler.POS_Y);
	}

	public static void registerMouseScrollX(String id)
	{
		registerInput(INPUT_ENGINE.getMouse(), id, MouseHandler.SCROLL_X);
	}

	public static void registerMouseScrollY(String id)
	{
		registerInput(INPUT_ENGINE.getMouse(), id, MouseHandler.SCROLL_Y);
	}

	public static void registerInput(InputHandler inputHandler, String id, int... handles)
	{
		for(int handle : handles)
		{
			registerInput(inputHandler, id, handle);
		}
	}

	public static void registerInput(InputHandler inputHandler, String id, int handle)
	{
		Input input = inputHandler.get(handle);
		if (input == null)
		{
			inputHandler.add(input = new Input(handle));
		}
		Input[] inputs = inputMapping.get(id);
		if (inputs == null)
		{
			inputs = new Input[] {input};
		}
		else
		{
			inputs = Arrays.copyOf(inputs, inputs.length + 1);
			inputs[inputs.length - 1] = input;
		}
		inputMapping.put(id, inputs);
	}
}
