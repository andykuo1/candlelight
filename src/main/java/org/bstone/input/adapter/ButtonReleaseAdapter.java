package org.bstone.input.adapter;

import org.bstone.input.InputState;
import org.bstone.input.device.event.ButtonEvent;
import org.bstone.input.device.event.InputEvent;
import org.bstone.input.device.input.ButtonInput;

/**
 * Created by Andy on 1/26/18.
 */
public class ButtonReleaseAdapter implements InputAdapter
{
	private final ButtonInput[] inputs;

	public ButtonReleaseAdapter(ButtonInput... inputs)
	{
		this.inputs = inputs;
	}

	@Override
	public boolean processInput(String intent, InputEvent event, InputState dst)
	{
		for(ButtonInput input : this.inputs)
		{
			if (input.isRelatedEvent(event))
			{
				ButtonEvent btn = (ButtonEvent) event;
				if (btn.state == 0 && !dst.getAction(intent, false))
				{
					dst.setAction(intent, true);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void poll(String intent, InputState dst)
	{
		dst.setAction(intent, false);
	}
}
