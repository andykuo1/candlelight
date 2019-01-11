package canary.bstone.input.device.input;

import canary.bstone.input.device.InputDevice;
import canary.bstone.input.device.event.AxisEvent;
import canary.bstone.input.device.event.InputEvent;

/**
 * Created by Andy on 1/26/18.
 */
public final class AxisInput extends Input
{
	public AxisInput(InputDevice src, int id)
	{
		super(src, id);
	}

	@Override
	public boolean isRelatedEvent(InputEvent event)
	{
		return event instanceof AxisEvent && super.isRelatedEvent(event);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof AxisInput && super.equals(obj);
	}
}
