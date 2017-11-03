package org.bstone.input;

import org.bstone.input.direct.AbstractInput;
import org.bstone.input.direct.AxisInput;
import org.bstone.input.direct.ButtonInput;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Andy on 10/13/17.
 */
public abstract class InputHandler
{
	private final Map<Integer, AxisInput> axes = new HashMap<>();
	private final Map<Integer, ButtonInput> buttons = new HashMap<>();

	public void poll()
	{
		this.getInputs().forEach(AbstractInput::poll);
	}

	protected final AxisInput createAxis(int id)
	{
		if (!this.isAxisID(id)) throw new IllegalArgumentException("not a valid axis id");

		AxisInput axis = new AxisInput(id);
		this.axes.put(axis.getID(), axis);
		return axis;
	}

	protected final ButtonInput createButton(int id)
	{
		if (!this.isButtonID(id)) throw new IllegalArgumentException("not a valid button id");

		ButtonInput button = new ButtonInput(id);
		this.buttons.put(button.getID(), button);
		return button;
	}

	protected final AxisInput destroyAxis(int id)
	{
		AxisInput axis = this.axes.remove(id);
		return axis;
	}

	protected final ButtonInput destroyButton(int id)
	{
		ButtonInput button = this.buttons.remove(id);
		return button;
	}

	public final AxisInput getAxis(int id)
	{
		if (!this.axes.containsKey(id))
		{
			return this.createAxis(id);
		}
		else
		{
			return this.axes.get(id);
		}
	}

	public final ButtonInput getButton(int id)
	{
		if (!this.buttons.containsKey(id))
		{
			return this.createButton(id);
		}
		else
		{
			return this.buttons.get(id);
		}
	}

	public final Stream<AbstractInput> getInputs()
	{
		return Stream.concat(this.axes.values().stream(), this.buttons.values().stream());
	}

	protected boolean isButtonID(int id)
	{
		return false;
	}

	protected boolean isAxisID(int id)
	{
		return false;
	}

	public void clear()
	{
		this.axes.clear();
		this.buttons.clear();
	}
}
