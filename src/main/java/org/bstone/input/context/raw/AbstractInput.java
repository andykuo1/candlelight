package org.bstone.input.context.raw;

/**
 * Created by Andy on 10/29/17.
 */
public abstract class AbstractInput
{
	private final int id;

	private boolean dirty = false;

	public AbstractInput(int id)
	{
		this.id = id;
	}

	public void poll()
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

	public int getID()
	{
		return this.id;
	}
}
