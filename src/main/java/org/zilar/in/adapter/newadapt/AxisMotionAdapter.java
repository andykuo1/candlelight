package org.zilar.in.adapter.newadapt;

import org.zilar.in.InputState;
import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.provider.AxisInput;

/**
 * Created by Andy on 1/23/18.
 */
public class AxisMotionAdapter extends InputRangeAdapter
{
	private final AxisInput[] inputs;
	private boolean consume;

	public AxisMotionAdapter(AxisInput...inputs)
	{
		this(false, inputs);
	}

	public AxisMotionAdapter(boolean consume, AxisInput...inputs)
	{
		this.consume = consume;
		this.inputs = inputs;
	}

	@Override
	public Float getRange(InputState inputState)
	{
		Float range = null;
		for(AxisInput input : this.inputs)
		{
			if (inputState.contains(input))
			{
				Float axis = inputState.getAxisChange(input.getID());
				if (axis != null)
				{
					if (this.consume)
					{
						inputState.consumeAxisChange(input.getID());
					}

					if (range == null) range = axis;
					else range += axis;
				}
			}
		}
		return range;
	}
}
