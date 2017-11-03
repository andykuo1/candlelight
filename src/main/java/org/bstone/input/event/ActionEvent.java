package org.bstone.input.event;

import org.bstone.input.adapter.InputAdapter;

/**
 * Created by Andy on 10/29/17.
 */
public class ActionEvent extends AbstractInputEvent
{
	protected int prev;
	protected int next;

	public ActionEvent(InputAdapter event)
	{
		super(event);
	}

	@Override
	public void poll()
	{
		int prevprev = this.prev;
		this.prev = this.next;

		Integer i = this.getAdapter().poll();
		if (i != null)
		{
			this.next = i;
		}

		if (prevprev != this.prev || this.prev != this.next)
		{
			super.poll();
		}
	}

	public boolean isPressedAndConsume()
	{
		if (this.isPressed())
		{
			this.consume();
			return true;
		}
		return false;
	}

	public boolean isReleasedAndConsume()
	{
		if (this.isReleased())
		{
			this.consume();
			return true;
		}
		return false;
	}

	public boolean isPressed()
	{
		return this.isDirty() && this.prev <= 0 && this.next > 0;
	}

	public boolean isReleased()
	{
		return this.isDirty() && this.prev > 0 && this.next <= 0;
	}

	public int getActionAndConsume()
	{
		int action = this.getAction();
		if (this.isDirty()) this.consume();
		return action;
	}

	public int getAction()
	{
		return this.isDirty() ? this.next : 0;
	}

	public int eventAction()
	{
		return this.next;
	}

	public int eventPrevious()
	{
		return this.prev;
	}

	public boolean eventActionChanged()
	{
		return this.next != this.prev;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InputAdapter<Integer> getAdapter()
	{
		return (InputAdapter<Integer>) super.getAdapter();
	}
}
