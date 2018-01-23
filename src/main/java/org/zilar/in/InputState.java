package org.zilar.in;

import org.zilar.in.handler.HandlerList;
import org.zilar.in.handler.InputHandler;
import org.zilar.in.provider.AxisInput;
import org.zilar.in.provider.ButtonInput;
import org.zilar.in.provider.InputProvider;

/**
 * Created by Andy on 1/22/18.
 */
public class InputState
{
	private final HandlerList<InputHandler> inputHandlers = new HandlerList<>();
	private final InputProvider provider;

	private final float[] prevAxes;
	private final float[] axes;

	private final int[] prevButtons;
	private final int[] buttons;

	private final boolean[] changeFlags;
	private final boolean[] flags;

	public InputState(InputProvider provider, int axisCount, int buttonCount)
	{
		this.provider = provider;

		this.prevAxes = new float[axisCount];
		this.axes = new float[axisCount];

		this.prevButtons = new int[buttonCount];
		this.buttons = new int[buttonCount];

		this.changeFlags = new boolean[this.axes.length + this.buttons.length];
		this.flags = new boolean[this.axes.length + this.buttons.length];
	}

	public InputState(InputState other)
	{
		this(other.provider, other.axes.length, other.buttons.length);

		this.copy(other);
	}

	public HandlerList<InputHandler> getInputHandlers()
	{
		return this.inputHandlers;
	}

	public void copy(InputState other)
	{
		for(int i = 0; i < this.axes.length && i < other.axes.length; ++i)
		{
			this.prevAxes[i] = other.prevAxes[i];
			this.axes[i] = other.axes[i];
			this.flags[i] = other.flags[i];
			this.changeFlags[i] = other.flags[i];
		}

		for(int i = 0; i < this.buttons.length && i < other.buttons.length; ++i)
		{
			this.prevButtons[i] = other.prevButtons[i];
			this.buttons[i] = other.buttons[i];
			this.flags[this.axes.length + i] = other.flags[other.axes.length + i];
			this.changeFlags[this.axes.length + i] = other.flags[other.axes.length + i];
		}
	}

	public void moveAxis(int id, float offset)
	{
		if (!this.onAxisUpdate(id, this.axes[id] + offset)) return;

		this.axes[id] += offset;
		this.flags[id] = true;
		this.changeFlags[id] = true;
	}

	public void setAxis(int id, float value)
	{
		if (!this.onAxisUpdate(id, value)) return;

		this.axes[id] = value;
		this.flags[id] = true;
		this.changeFlags[id] = true;
	}

	protected boolean onAxisUpdate(int id, float newValue)
	{
		float oldValue = this.axes[id];
		for(InputHandler handler : this.inputHandlers)
		{
			if (handler.onAxisUpdate(this, id, oldValue, newValue))
			{
				return false;
			}
		}
		return true;
	}

	public void pressButton(int id)
	{
		if (!this.onButtonUpdate(id, 1)) return;

		this.buttons[id] = 1;
		this.flags[this.axes.length + id] = true;
		this.changeFlags[this.axes.length + id] = true;
	}

	public void releaseButton(int id)
	{
		if (!this.onButtonUpdate(id, 0)) return;

		this.buttons[id] = 0;
		this.flags[this.axes.length + id] = true;
		this.changeFlags[this.axes.length + id] = true;
	}

	public void setButton(int id, int state)
	{
		if (!this.onButtonUpdate(id, state)) return;

		this.buttons[id] = state;
		this.flags[this.axes.length + id] = true;
		this.changeFlags[this.axes.length + id] = true;
	}

	protected boolean onButtonUpdate(int id, int newValue)
	{
		int oldValue = this.buttons[id];
		for(InputHandler handler : this.inputHandlers)
		{
			if (handler.onButtonUpdate(this, id, oldValue, newValue))
			{
				return false;
			}
		}
		return true;
	}

	public Float getAxis(int id)
	{
		return this.flags[id] ? this.axes[id] : null;
	}

	public Float getAxisChange(int id)
	{
		return this.changeFlags[id] ? this.axes[id] - this.prevAxes[id] : null;
	}

	public Integer getButton(int id)
	{
		return this.flags[this.axes.length + id] ? this.buttons[id] : null;
	}

	public Integer getButtonChange(int id)
	{
		return this.changeFlags[this.axes.length + id] ? this.buttons[id] - this.prevButtons[id] : null;
	}

	public void consumeAxis(int id)
	{
		this.flags[id] = false;
	}

	public void consumeAxisChange(int id)
	{
		this.changeFlags[id] = false;
	}

	public void consumeButton(int id)
	{
		this.flags[this.axes.length + id] = false;
	}

	public void consumeButtonChange(int id)
	{
		this.changeFlags[this.axes.length + id] = false;
	}

	public void consumeAll()
	{
		for(int i = 0; i < this.flags.length; ++i)
		{
			this.flags[i] = false;
			this.changeFlags[i] = false;
		}
	}

	public boolean contains(AxisInput axis)
	{
		return axis.getProvider().equals(this.provider);
	}

	public boolean contains(ButtonInput button)
	{
		return button.getProvider().equals(this.provider);
	}

	public InputProvider getProvider()
	{
		return this.provider;
	}
}
