package org.bstone.input.context.event;

import org.bstone.input.context.adapter.IRange;
import org.bstone.input.context.adapter.InputAdapter;

/**
 * Created by Andy on 10/29/17.
 */
public class RangeEvent extends AbstractInputEvent
{
	protected float prev;
	protected float next;

	public RangeEvent(InputAdapter event)
	{
		super(event);
	}

	@Override
	public void poll()
	{
		super.poll();
		this.prev = this.next;
		Float f = this.getAdapter().poll();
		if (f != null)
		{
			this.next = f;
		}
	}

	public float getRangeAndConsume()
	{
		float range = this.getRange();
		if (this.isDirty()) this.consume();
		return range;
	}

	public float getMotionAndConsume()
	{
		float motion = this.getMotion();
		if (this.isDirty()) this.consume();
		return motion;
	}

	public float getRange()
	{
		return this.isDirty() ? this.eventRange() : 0;
	}

	public float getMotion()
	{
		return this.isDirty() ? this.eventMotion() : 0;
	}

	public float eventRange()
	{
		return this.next;
	}

	public float eventPrevious()
	{
		return this.prev;
	}

	public float eventMotion()
	{
		return this.next - this.prev;
	}

	@Override
	public IRange getAdapter()
	{
		return (IRange) super.getAdapter();
	}
}
