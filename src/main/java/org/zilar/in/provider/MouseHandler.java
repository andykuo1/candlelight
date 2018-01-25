package org.zilar.in.provider;

import org.bstone.newinput.device.Mouse;
import org.bstone.newinput.device.event.AxisEvent;
import org.bstone.newinput.device.event.ButtonEvent;
import org.bstone.newinput.device.event.InputEvent;
import org.bstone.newinput.device.event.InputEventListener;
import org.zilar.in.state.RawInputState;

/**
 * Created by Andy on 1/24/18.
 */
public class MouseHandler implements InputEventListener, InputProvider
{
	private final RawInputState providerState;

	public MouseHandler()
	{
		this.providerState = new RawInputState(this, Mouse.AXIS_LAST + 1, Mouse.BUTTON_LAST + 1);
	}

	@Override
	public boolean onInputEvent(InputEvent evt)
	{
		if (evt.src instanceof Mouse)
		{
			if (evt instanceof ButtonEvent)
			{
				switch(((ButtonEvent) evt).state)
				{
					case ButtonEvent.STATE_RELEASE:
						this.providerState.releaseButton(evt.id);
						break;
					case ButtonEvent.STATE_PRESS:
						this.providerState.pressButton(evt.id);
						break;
					default:
						return false;
				}
				return true;
			}
			else if (evt instanceof AxisEvent)
			{
				switch (evt.id)
				{
					case Mouse.AXIS_CURSORX:
						this.providerState.setAxis(Mouse.AXIS_CURSORX, ((AxisEvent) evt).value);
						break;
					case Mouse.AXIS_CURSORY:
						this.providerState.setAxis(Mouse.AXIS_CURSORY, ((AxisEvent) evt).value);
						break;
					case Mouse.AXIS_SCROLLX:
						this.providerState.offsetAxis(Mouse.AXIS_SCROLLX, ((AxisEvent) evt).value);
						break;
					case Mouse.AXIS_SCROLLY:
						this.providerState.offsetAxis(Mouse.AXIS_SCROLLY, ((AxisEvent) evt).value);
						break;
					default:
						return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public RawInputState getProviderState()
	{
		return this.providerState;
	}
}
