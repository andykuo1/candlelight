package canary.bstone.input.device;

import canary.bstone.input.device.event.InputEvent;
import canary.bstone.input.device.event.InputEventListener;
import canary.bstone.input.device.event.TextEvent;

/**
 * Created by Andy on 1/26/18.
 */
public class InputTextHandler implements InputEventListener
{
	private final StringBuffer sb = new StringBuffer();

	@Override
	public boolean onInputEvent(InputEvent evt)
	{
		if (evt instanceof TextEvent)
		{
			this.sb.append((char) ((TextEvent) evt).codepoint);
			return true;
		}
		return false;
	}

	public StringBuffer getStringBuffer()
	{
		return this.sb;
	}

	public void clearBuffer()
	{
		this.sb.setLength(0);
	}
}
