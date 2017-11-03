package org.bstone.input.event;

import org.bstone.input.adapter.InputAdapter;

/**
 * Created by Andy on 10/29/17.
 */
public abstract class AbstractInputEvent
{
	private final InputAdapter adapter;

	private boolean dirty;

	public AbstractInputEvent(InputAdapter adapter)
	{
		this.adapter = adapter;
	}

	public void poll()
	{
		this.markDirty();
	}

	public final void consume()
	{
		this.dirty = false;
	}

	public final void markDirty()
	{
		this.dirty = true;
	}

	public final boolean isDirty()
	{
		return this.dirty;
	}

	public InputAdapter getAdapter()
	{
		return this.adapter;
	}
}
