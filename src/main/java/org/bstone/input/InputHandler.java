package org.bstone.input;

import org.bstone.input.mapping.AxisInput;
import org.bstone.input.mapping.ButtonInput;
import org.bstone.input.mapping.Input;
import org.bstone.input.mapping.VirtualAxis;
import org.bstone.input.mapping.VirtualButtonGroup;
import org.bstone.input.mapping.VirtualInput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by Andy on 10/13/17.
 */
public abstract class InputHandler
{
	private final Map<Integer, AxisInput> axes = new HashMap<>();
	private final Map<Integer, ButtonInput> buttons = new HashMap<>();
	private final Set<VirtualInput> virtuals = new HashSet<>();

	public void update()
	{
		for(VirtualInput virtual : this.virtuals)
		{
			virtual.update();
		}
	}

	public final AxisInput createAxis(int id)
	{
		if (!this.isRawAxisID(id)) throw new IllegalArgumentException("not a valid axis id");

		AxisInput axis = new AxisInput(id);
		this.axes.put(axis.id, axis);
		return axis;
	}

	public final VirtualAxis createAxis(int id, int positive, int negative)
	{
		if (this.isRawAxisID(id)) throw new IllegalArgumentException("not a valid virtual axis id");

		VirtualAxis axis = new VirtualAxis(id, this.getButton(positive), this.getButton(negative));
		this.axes.put(axis.id, axis);
		this.virtuals.add(axis);
		return axis;
	}

	public final ButtonInput createButton(int id)
	{
		if (!this.isRawButtonID(id)) throw new IllegalArgumentException("not a valid button id");

		ButtonInput button = new ButtonInput(id);
		this.buttons.put(button.id, button);
		return button;
	}

	public final VirtualButtonGroup createButtonGroup(int id, int... ids)
	{
		if (this.isRawButtonID(id)) throw new IllegalArgumentException("not a valid virtual button group id");

		Input[] inputs = new Input[ids.length];
		int i = 0;
		for(int input : ids)
		{
			if (!this.isRawButtonID(input))
				throw new IllegalArgumentException("not a valid button id");

			ButtonInput button = this.getButton(input);
			inputs[i++] = button;
		}

		VirtualButtonGroup buttonGroup = new VirtualButtonGroup(id, inputs);
		this.buttons.put(buttonGroup.id, buttonGroup);
		this.virtuals.add(buttonGroup);
		return buttonGroup;
	}

	public final AxisInput destroyAxis(int id)
	{
		AxisInput axis = this.axes.remove(id);
		if (axis instanceof VirtualInput)
		{
			this.virtuals.remove(axis);
		}
		return axis;
	}

	public final ButtonInput destroyButton(int id)
	{
		ButtonInput button = this.buttons.remove(id);
		if (button instanceof VirtualInput)
		{
			this.virtuals.remove(button);
		}
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

	public final Stream<Input> getInputs()
	{
		return Stream.concat(this.axes.values().stream(), this.buttons.values().stream());
	}

	protected boolean isRawButtonID(int id)
	{
		return false;
	}

	protected boolean isRawAxisID(int id)
	{
		return false;
	}

	public void clear()
	{
		this.virtuals.clear();
		this.axes.clear();
		this.buttons.clear();
	}
}
