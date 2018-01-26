package org.zilar.in4.adapter;

import org.bstone.newinput.device.event.ButtonEvent;
import org.bstone.newinput.device.event.InputEvent;
import org.bstone.newinput.device.input.ButtonInput;
import org.zilar.in4.InputAdapter;
import org.zilar.in4.InputState;

/**
 * Created by Andy on 1/26/18.
 */
public class ButtonUpAdapter implements InputAdapter
{
	private final ButtonInput[] inputs;

	public ButtonUpAdapter(ButtonInput... inputs)
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
				dst.setState(intent, btn.state == 0);
				return true;
			}
		}
		return false;
	}
}
