package boron.base.window.input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Andy on 8/3/17.
 */
@Deprecated
public class InputManager
{
	protected final InputEngine inputEngine;
	private final Map<String, Input[]> inputMapping = new HashMap<>();

	public InputManager(InputEngine inputEngine)
	{
		this.inputEngine = inputEngine;
	}

	public Input[] getInput(String id)
	{
		if (!this.inputMapping.containsKey(id)) throw new NoSuchElementException("Cannot find input with id '" + id + "'");

		return this.inputMapping.get(id);
	}

	public boolean isInputDown(String id)
	{
		if (!this.inputMapping.containsKey(id)) throw new NoSuchElementException("Cannot find input with id '" + id + "'");

		Input[] inputs = this.inputMapping.get(id);
		for(Input input : inputs)
		{
			if (input.isDown())
			{
				return true;
			}
		}

		return false;
	}

	public boolean isInputPressed(String id)
	{
		if (!this.inputMapping.containsKey(id)) throw new NoSuchElementException("Cannot find input with id '" + id + "'");

		Input[] inputs = this.inputMapping.get(id);
		for(Input input : inputs)
		{
			if (input.isPressed())
			{
				return true;
			}
		}

		return false;
	}

	public boolean isInputReleased(String id)
	{
		if (!this.inputMapping.containsKey(id)) throw new NoSuchElementException("Cannot find input with id '" + id + "'");

		Input[] inputs = this.inputMapping.get(id);
		for(Input input : inputs)
		{
			if (input.isReleased())
			{
				return true;
			}
		}

		return false;
	}

	public float getInputAmount(String id)
	{
		if (!this.inputMapping.containsKey(id)) throw new NoSuchElementException("Cannot find input with id '" + id + "'");

		Input[] inputs = this.inputMapping.get(id);
		for(Input input : inputs)
		{
			float f = input.getAmount();
			if (f != 0) return f;
		}

		return 0;
	}

	public float getInputMotion(String id)
	{
		Input[] inputs = this.inputMapping.get(id);
		for(Input input : inputs)
		{
			float f = input.getMotion();
			if (f != 0) return f;
		}

		return 0;
	}

	public void consumeInput(String id)
	{
		if (!this.inputMapping.containsKey(id)) throw new NoSuchElementException("Cannot find input with id '" + id + "'");

		Input[] inputs = this.inputMapping.get(id);
		for(Input input : inputs)
		{
			input.consume();
		}
	}

	public void registerKey(String id, int... keycode)
	{
		this.registerInput(this.inputEngine.getKeyboard(), id, keycode);
	}

	public void registerMouse(String id, int... button)
	{
		this.registerInput(this.inputEngine.getMouse(), id, button);
	}

	public void registerMousePosX(String id)
	{
		this.registerInput(this.inputEngine.getMouse(), id, MouseHandler.POS_X);
	}

	public void registerMousePosY(String id)
	{
		this.registerInput(this.inputEngine.getMouse(), id, MouseHandler.POS_Y);
	}

	public void registerMouseScrollX(String id)
	{
		this.registerInput(this.inputEngine.getMouse(), id, MouseHandler.SCROLL_X);
	}

	public void registerMouseScrollY(String id)
	{
		this.registerInput(this.inputEngine.getMouse(), id, MouseHandler.SCROLL_Y);
	}

	public void registerInput(InputHandler inputHandler, String id, int... handles)
	{
		for(int handle : handles)
		{
			this.registerInput(inputHandler, id, handle);
		}
	}

	public void registerInput(InputHandler inputHandler, String id, int handle)
	{
		Input input = inputHandler.get(handle);
		if (input == null)
		{
			inputHandler.add(input = new Input(handle));
		}
		Input[] inputs = this.inputMapping.get(id);
		if (inputs == null)
		{
			inputs = new Input[] {input};
		}
		else
		{
			inputs = Arrays.copyOf(inputs, inputs.length + 1);
			inputs[inputs.length - 1] = input;
		}
		this.inputMapping.put(id, inputs);
	}

	public void clear()
	{
		this.inputMapping.clear();
	}
}
