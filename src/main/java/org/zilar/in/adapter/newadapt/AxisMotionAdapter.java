package org.zilar.in.adapter.newadapt;

import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.input.AxisInput;
import org.zilar.in.state.InputState;

/**
 * Created by Andy on 1/23/18.
 */
public class AxisMotionAdapter implements InputRangeAdapter
{
	private final AxisInput[] inputs;
	private boolean consume;

	private float motion;

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
	public void update(InputState inputState)
	{
		Float f = null;
		for(AxisInput input : this.inputs)
		{
			if (inputState.contains(input))
			{
				Float motion = inputState.getMotion(input);
				if (motion != null)
				{
					if (this.consume)
					{
						inputState.consume(input);
					}
					
					if (f == null) f = motion;
					else f += motion;
				}
			}
		}

		if (f != null)
		{
			this.motion = f;
		}
	}

	@Override
	public float getRange()
	{
		return this.motion;
	}
}
