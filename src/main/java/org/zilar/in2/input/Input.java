package org.zilar.in2.input;

import org.bstone.newinput.device.InputDevice;

/**
 * Created by Andy on 1/24/18.
 */
public abstract class Input
{
	public InputDevice src;
	public int id;

	public Input(InputDevice src, int id)
	{
		this.src = src;
		this.id = id;
	}
}
