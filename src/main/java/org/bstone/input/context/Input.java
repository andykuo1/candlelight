package org.bstone.input.context;

/**
 * Created by Andy on 10/13/17.
 */
public abstract class Input
{
	public final int id;

	private boolean dirty = false;

	public Input(int id)
	{
		this.id = id;
	}

	public void clean()
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
}
