package canary.bstone.input.adapter;

import canary.bstone.input.InputState;
import canary.bstone.input.device.event.ButtonEvent;
import canary.bstone.input.device.event.InputEvent;
import canary.bstone.input.device.input.ButtonInput;

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
				return false;//TODO: this should be true, but it cant...
			}
		}
		return false;
	}
}
