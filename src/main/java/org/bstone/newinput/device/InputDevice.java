package org.bstone.newinput.device;


import org.bstone.newinput.device.event.InputEvent;
import org.bstone.newinput.device.input.AxisInput;
import org.bstone.newinput.device.input.ButtonInput;

/**
 * Created by Andy on 1/23/18.
 */
public interface InputDevice
{
	void fireEvent(InputEvent event);

	default AxisInput getAxis(int id)
	{
		return new AxisInput(this, id);
	}

	default ButtonInput getButton(int id)
	{
		return new ButtonInput(this, id);
	}

	String getName();
}
