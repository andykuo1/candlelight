package org.bstone.input.event;

import org.bstone.input.adapter.InputAdapter;

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
		this.prev = this.next;

		Float f = this.getAdapter().poll();
		if (f != null)
		{
			this.next = f;

			super.poll();
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

	@SuppressWarnings("unchecked")
	@Override
	public InputAdapter<Float> getAdapter()
	{
		return (InputAdapter<Float>) super.getAdapter();
	}
}
