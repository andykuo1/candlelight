package org.zilar.in4.adapter;

import org.bstone.newinput.device.event.InputEvent;
import org.bstone.newinput.device.input.ButtonInput;
import org.zilar.in4.InputAdapter;
import org.zilar.in4.InputState;

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
		Float range = null;
		if (this.positive.processInput(intent + ".up", event, dst))
		{
			boolean down = dst.getState(intent + ".up");
			if (down) range = 1F;
			else range = 0F;
		}
		if (this.negative.processInput(intent + ".down", event, dst))
		{
			boolean down = dst.getState(intent + ".down");
			if (down)
			{
				if (range == null) range = -1F;
				else range += -1F;
			}
		}

		if (range != null)
		{
			dst.setRange(intent, range);
			return true;
		}

		return false;
	}
}
