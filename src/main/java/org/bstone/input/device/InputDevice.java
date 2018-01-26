package org.bstone.input.device;


import org.bstone.input.device.event.InputEvent;
import org.bstone.input.device.input.AxisInput;
import org.bstone.input.device.input.ButtonInput;

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
