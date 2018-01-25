package org.zilar.in.adapter.newadapt;

import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.input.AxisInput;
import org.zilar.in.state.InputState;

/**
 * Created by Andy on 1/23/18.
 */
public class AxisAdapter implements InputRangeAdapter
{
	private final AxisInput[] inputs;
	private boolean consume;

	private float range;

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
	public void update(InputState inputState)
	{
		Float f = null;
		for(AxisInput input : this.inputs)
		{
			if (inputState.contains(input))
			{
				Float axis = inputState.get(input);
				if (axis != null)
				{
					if (this.consume)
					{
						inputState.consume(input);
					}

					if (f == null) f = axis;
					else f += axis;
				}
			}
		}

		if (f != null)
		{
			this.range = f;
		}
	}

	@Override
	public float getRange()
	{
		return this.range;
	}
}
