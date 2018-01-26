package org.bstone.input.adapter;

import org.bstone.input.InputState;
import org.bstone.input.device.event.AxisEvent;
import org.bstone.input.device.event.InputEvent;
import org.bstone.input.device.input.AxisInput;

/**
 * Created by Andy on 1/26/18.
 */
public class AxisMotionAdapter implements InputAdapter
{
	private final AxisInput input;

	public AxisMotionAdapter(AxisInput input)
	{
		this.input = input;
	}

	@Override
	public boolean processInput(String intent, InputEvent event, InputState dst)
	{
		if (this.input.isRelatedEvent(event))
		{
			AxisEvent axis = (AxisEvent) event;
			dst.setRange(intent, axis.value - dst.getRange(intent + ".prev"));
			dst.setRange(intent + ".prev", axis.value);
			return true;
		}
		return false;
	}

	@Override
	public void poll(String intent, InputState dst)
	{
		dst.setRange(intent, 0);
	}
}
