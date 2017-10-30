package org.bstone.input.context.event;

import org.bstone.input.context.adapter.InputAdapter;

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
		super.poll();
		this.prev = this.next;
		Integer i = this.getAdapter().poll();
		if (i != null)
		{
			this.next = i;
		}
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

	@SuppressWarnings("unchecked")
	@Override
	public InputAdapter<Integer> getAdapter()
	{
		return (InputAdapter<Integer>) super.getAdapter();
	}
}
