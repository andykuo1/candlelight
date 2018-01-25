package org.bstone.newinput.device.event;

import org.bstone.newinput.device.InputDevice;

/**
 * Created by Andy on 1/23/18.
 */
public final class ButtonEvent extends InputEvent
{
	public static final int STATE_RELEASE = 0;
	public static final int STATE_PRESS = 1;

	public int state;

	ButtonEvent(InputDevice src, int id)
	{
		super(src, id);
	}
}
