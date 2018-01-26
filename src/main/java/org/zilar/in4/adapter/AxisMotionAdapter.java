package org.zilar.in4.adapter;

import org.bstone.newinput.device.event.AxisEvent;
import org.bstone.newinput.device.event.InputEvent;
import org.bstone.newinput.device.input.AxisInput;
import org.zilar.in4.InputAdapter;
import org.zilar.in4.InputState;

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
			float range = dst.getRange(intent);
			dst.setRange(intent, axis.value - range);
			return true;
		}
		return false;
	}
}
