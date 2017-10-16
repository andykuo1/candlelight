package org.bstone.input;

import org.bstone.input.context.AxisInput;
import org.bstone.input.context.ButtonInput;
import org.bstone.input.context.IVirtual;
import org.bstone.input.context.Input;
import org.bstone.input.context.VirtualAxis;

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
	private final Set<IVirtual> virtuals = new HashSet<>();

	public void update()
	{
		for(IVirtual virtual : this.virtuals)
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

	public final AxisInput destroyAxis(int id)
	{
		AxisInput axis = this.axes.remove(id);
		if (axis instanceof IVirtual)
		{
			this.virtuals.remove(axis);
		}
		return axis;
	}

	public final ButtonInput destroyButton(int id)
	{
		ButtonInput button = this.buttons.remove(id);
		if (button instanceof IVirtual)
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
