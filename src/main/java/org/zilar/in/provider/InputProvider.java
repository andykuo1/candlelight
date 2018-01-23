package org.zilar.in.provider;

import org.zilar.in.InputState;

/**
 * Created by Andy on 1/22/18.
 */
public interface InputProvider
{
	default AxisInput getAxis(int id)
	{
		return new AxisInput(this, id);
	}

	default ButtonInput getButton(int id)
	{
		return new ButtonInput(this, id);
	}

	InputState getInputState();
}
