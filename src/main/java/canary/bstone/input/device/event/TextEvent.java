package canary.bstone.input.device.event;

import canary.bstone.input.device.InputDevice;

/**
 * Created by Andy on 1/23/18.
 */
public final class TextEvent extends InputEvent
{
	public int codepoint;

	TextEvent(InputDevice src, int id)
	{
		super(src, id);
	}

	@Override
	public String getEventID()
	{
		return super.getEventID() + ":TEXT";
	}
}
