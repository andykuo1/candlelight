package canary.bstone.input.device.event;

import canary.bstone.input.device.InputDevice;

/**
 * Created by Andy on 1/23/18.
 */
public final class AxisEvent extends InputEvent
{
	public float value;

	AxisEvent(InputDevice src, int id)
	{
		super(src, id);
	}

	@Override
	public String getEventID()
	{
		return super.getEventID() + ":AXIS";
	}
}
