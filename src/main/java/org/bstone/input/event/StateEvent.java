package org.bstone.input.event;

import org.bstone.input.adapter.InputAdapter;

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
		boolean prevprev = this.prev;
		this.prev = this.next;

		Boolean b = this.getAdapter().poll();
		if (b != null)
		{
			this.next = b;
		}

		if (prevprev != this.prev || this.prev != this.next)
		{
			super.poll();
		}
	}

	public boolean isUpAndConsume()
	{
		if (this.isUp())
		{
			this.consume();
			return true;
		}
		return false;
	}

	public boolean isDownAndConsume()
	{
		if (this.isDown())
		{
			this.consume();
			return true;
		}
		return false;
	}

	public boolean isUp()
	{
		return this.isDirty() && !this.next;
	}

	public boolean isDown()
	{
		return this.isDirty() && this.next;
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
