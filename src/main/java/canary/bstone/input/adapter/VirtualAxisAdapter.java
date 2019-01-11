package canary.bstone.input.adapter;

import canary.bstone.input.InputState;
import canary.bstone.input.device.event.InputEvent;
import canary.bstone.input.device.input.ButtonInput;

/**
 * Created by Andy on 1/26/18.
 */
public class VirtualAxisAdapter implements InputAdapter
{
	private final ButtonDownAdapter positive;
	private final ButtonDownAdapter negative;

	public VirtualAxisAdapter(ButtonInput positive, ButtonInput negative)
	{
		this.positive = new ButtonDownAdapter(positive);
		this.negative = new ButtonDownAdapter(negative);
	}

	@Override
	public boolean processInput(String intent, InputEvent event, InputState dst)
	{
		float range = 0;
		this.positive.processInput(intent + ".up", event, dst);
		this.negative.processInput(intent + ".down", event, dst);

		if (dst.getState(intent + ".up"))
		{
			range += 1;
		}

		if (dst.getState(intent + ".down"))
		{
			range -= 1;
		}

		if (dst.getRange(intent) != range)
		{
			dst.setRange(intent, range);
		}

		return false;
	}
}
