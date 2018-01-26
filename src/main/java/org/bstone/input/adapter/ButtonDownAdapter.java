package org.bstone.input.adapter;

import org.bstone.input.InputState;
import org.bstone.input.device.event.ButtonEvent;
import org.bstone.input.device.event.InputEvent;
import org.bstone.input.device.input.ButtonInput;

/**
 * Created by Andy on 1/26/18.
 */
public class ButtonDownAdapter implements InputAdapter
{
	private final ButtonInput[] inputs;

	public ButtonDownAdapter(ButtonInput... inputs)
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
				dst.setState(intent, btn.state == 1);
				return false;//TODO: this should be true, but it cant...
			}
		}
		return false;
	}
}
