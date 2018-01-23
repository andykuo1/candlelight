package org.zilar.in.adapter.newadapt;

import org.zilar.in.InputState;
import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.provider.AxisInput;

/**
 * Created by Andy on 1/23/18.
 */
public class AxisAdapter extends InputRangeAdapter
{
	private final AxisInput[] inputs;
	private boolean consume;

	public AxisAdapter(AxisInput...inputs)
	{
		this(false, inputs);
	}

	public AxisAdapter(boolean consume, AxisInput...inputs)
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
				Float axis = inputState.getAxis(input.getID());
				if (axis != null)
				{
					if (this.consume)
					{
						inputState.consumeAxis(input.getID());
					}

					if (range == null) range = axis;
					else range += axis;
				}
			}
		}
		return range;
	}
}
