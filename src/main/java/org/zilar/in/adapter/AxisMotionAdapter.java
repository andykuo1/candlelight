package org.zilar.in.adapter;

import org.zilar.in.InputState;
import org.zilar.in.provider.InputProvider;

/**
 * Created by Andy on 1/22/18.
 */
public class AxisMotionAdapter extends InputAdapter
{
	private final int[] axes;

	public AxisMotionAdapter(InputProvider provider, int... axes)
	{
		super(provider);
		this.axes = axes;
	}

	@Override
	public Boolean getState(InputState inputState)
	{
		for(int i = 0; i < this.axes.length; ++i)
		{
			int id = this.axes[i];
			Float change = inputState.getAxisChange(id);
			if (change != null)
			{
				inputState.consumeAxis(id);
				return change != 0;
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
			Float axis = inputState.getAxisChange(id);
			if (axis != null)
			{
				inputState.consumeAxis(id);
				return axis > 0 ? 1 : axis < 0 ? -1 : 0;
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
			Float change = inputState.getAxisChange(id);
			if (change != null)
			{
				inputState.consumeAxis(id);
				return change;
			}
		}
		return null;
	}
}
