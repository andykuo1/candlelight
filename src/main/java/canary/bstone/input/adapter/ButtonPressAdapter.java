package canary.bstone.input.adapter;

import canary.bstone.input.InputState;
import canary.bstone.input.device.event.ButtonEvent;
import canary.bstone.input.device.event.InputEvent;
import canary.bstone.input.device.input.ButtonInput;

/**
 * Created by Andy on 1/26/18.
 */
public class ButtonPressAdapter implements InputAdapter
{
	private final ButtonInput[] inputs;

	public ButtonPressAdapter(ButtonInput... inputs)
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
				if (btn.state == 1 && !dst.getAction(intent, false))
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
