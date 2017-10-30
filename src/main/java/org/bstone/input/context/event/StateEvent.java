package org.bstone.input.context.event;

import org.bstone.input.context.adapter.InputAdapter;

/**
 * Created by Andy on 10/29/17.
 */
public class StateEvent extends AbstractInputEvent
{
	protected boolean prev;
	protected boolean next;

	public StateEvent(InputAdapter event)
	{
		super(event);
	}

	@Override
	public void poll()
	{
		super.poll();
		this.prev = this.next;
		Boolean b = this.getAdapter().poll();
		if (b != null)
		{
			this.next = b;
		}
	}

	public boolean isUpAndConsume()
	{
		boolean b = this.isUp();
		if (this.isDirty()) this.consume();
		return b;
	}

	public boolean isDownAndConsume()
	{
		boolean b = this.isDown();
		if (this.isDirty()) this.consume();
		return b;
	}

	public boolean isPressedAndConsume()
	{
		boolean b = this.isPressed();
		if (this.isDirty()) this.consume();
		return b;
	}

	public boolean isReleasedAndConsume()
	{
		boolean b = this.isReleased();
		if (this.isDirty()) this.consume();
		return b;
	}

	public boolean isUp()
	{
		return this.isDirty() && !this.next;
	}

	public boolean isDown()
	{
		return this.isDirty() && this.next;
	}

	public boolean isPressed()
	{
		return this.isDirty() && this.eventStateChanged() && this.next;
	}

	public boolean isReleased()
	{
		return this.isDirty() && this.eventStateChanged() && !this.next;
	}

	public boolean eventState()
	{
		return this.next;
	}

	public boolean eventPrevious()
	{
		return this.prev;
	}

	public boolean eventStateChanged()
	{
		return this.prev != this.next;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InputAdapter<Boolean> getAdapter()
	{
		return (InputAdapter<Boolean>) super.getAdapter();
	}
}
