package org.bstone.input.mapping;

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

	public abstract float getMotion();
	public abstract float getRange();

	public abstract int getPreviousAction();
	public abstract int getAction();

	public boolean isStateChanged()
	{
		return this.getPreviousAction() != this.getAction();
	}

	public boolean getState()
	{
		return this.getAction() != 0;
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
