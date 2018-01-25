package org.bstone.newinput.device;


import org.bstone.newinput.device.event.InputEvent;

/**
 * Created by Andy on 1/23/18.
 */
public interface InputDevice
{
	void fireEvent(InputEvent event);
}
