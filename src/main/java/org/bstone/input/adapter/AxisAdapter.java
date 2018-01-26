package org.bstone.input.adapter;

import org.bstone.input.InputState;
import org.bstone.input.device.event.AxisEvent;
import org.bstone.input.device.event.InputEvent;
import org.bstone.input.device.input.AxisInput;

/**
 * Created by Andy on 1/26/18.
 */
public class AxisAdapter implements InputAdapter
{
	private final AxisInput input;

	public AxisAdapter(AxisInput input)
	{
		this.input = input;
	}

	@Override
	public boolean processInput(String intent, InputEvent event, InputState dst)
	{
		if (this.input.isRelatedEvent(event))
		{
			AxisEvent axis = (AxisEvent) event;
			dst.setRange(intent, axis.value);
			return false; //TODO: this should be true, but it cant...
		}
		return false;
	}
}
