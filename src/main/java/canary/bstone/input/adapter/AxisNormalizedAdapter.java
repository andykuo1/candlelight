package canary.bstone.input.adapter;

import canary.bstone.input.InputState;
import canary.bstone.input.device.event.AxisEvent;
import canary.bstone.input.device.event.InputEvent;
import canary.bstone.input.device.input.AxisInput;

/**
 * Created by Andy on 1/26/18.
 */
public class AxisNormalizedAdapter implements InputAdapter
{
	private final AxisInput input;
	private final float max;

	public AxisNormalizedAdapter(float max, AxisInput input)
	{
		this.max = max;
		this.input = input;
	}

	@Override
	public boolean processInput(String intent, InputEvent event, InputState dst)
	{
		if (this.input.isRelatedEvent(event))
		{
			AxisEvent axis = (AxisEvent) event;
			dst.setRange(intent, axis.value / this.max);
			return false;//TODO: this should be true, but it cant...
		}
		return false;
	}
}
