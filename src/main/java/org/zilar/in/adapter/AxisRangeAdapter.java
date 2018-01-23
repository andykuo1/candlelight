package org.zilar.in.adapter;

import org.zilar.in.InputState;
import org.zilar.in.provider.InputProvider;

/**
 * Created by Andy on 1/22/18.
 */
public class AxisRangeAdapter extends InputAdapter
{
	private final int[] axes;
	private final boolean consume;

	public AxisRangeAdapter(InputProvider provider, boolean consume, int... axes)
	{
		super(provider);
		this.axes = axes;
		this.consume = consume;
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
				if (this.consume) inputState.consumeAxis(id);
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
			Float change = inputState.getAxisChange(id);
			if (change != null)
			{
				if (this.consume) inputState.consumeAxis(id);
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
				if (this.consume) inputState.consumeAxis(id);
				return axis;
			}
		}
		return null;
	}
}
