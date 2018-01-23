package org.zilar.in.adapter;

import org.zilar.in.InputProvider;
import org.zilar.in.InputState;

/**
 * Created by Andy on 1/22/18.
 */
public class AxisRangeAdapter extends InputAdapter
{
	private final int[] axes;

	public AxisRangeAdapter(String name, InputProvider provider, int... axes)
	{
		super(name, provider);
		this.axes = axes;
	}

	@Override
	public Boolean getState(InputState inputState)
	{
		for(int i = 0; i < this.axes.length; ++i)
		{
			int id = this.axes[i];
			Float axis = inputState.getAxis(id);
			if (axis != null)
			{
				inputState.consumeAxis(id);
				return axis != 0;
			}
		}
		return null;
	}

	@Override
	public Integer getAction(InputState inputState)
	{
		for(int i = 0; i < this.axes.length; ++i)
		{
			int id = this.axes[i];
			Float change = inputState.getAxisChange(id);
			if (change != null)
			{
				inputState.consumeAxis(id);
				return change > 0 ? 1 : change < 0 ? -1 : 0;
			}
		}
		return null;
	}

	@Override
	public Float getRange(InputState inputState)
	{
		for(int i = 0; i < this.axes.length; ++i)
		{
			int id = this.axes[i];
			Float axis = inputState.getAxis(id);
			if (axis != null)
			{
				inputState.consumeAxis(id);
				return axis;
			}
		}
		return null;
	}
}
