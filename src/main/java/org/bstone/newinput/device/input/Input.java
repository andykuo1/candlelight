package org.bstone.newinput.device.input;

import org.bstone.newinput.device.InputDevice;
import org.bstone.newinput.device.event.InputEvent;

/**
 * Created by Andy on 1/26/18.
 */
public abstract class Input
{
	private final InputDevice src;
	private final int id;

	public Input(InputDevice src, int id)
	{
		this.src = src;
		this.id = id;
	}

	public boolean isRelatedEvent(InputEvent event)
	{
		return this.src.equals(event.src) && this.id == event.id;
	}

	public InputDevice getSource()
	{
		return this.src;
	}

	public int getID()
	{
		return this.id;
	}

	@Override
	public int hashCode()
	{
		return this.src.hashCode() + (this.id >> 8);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Input &&
				this.src == ((Input) obj).src &&
				this.id == ((Input) obj).id;
	}
}
