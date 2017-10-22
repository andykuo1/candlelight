package org.bstone.input.mapping;

/**
 * Created by Andy on 10/13/17.
 */
public class ConditionalInput extends Input
{
	protected boolean prev;
	protected boolean next;

	public ConditionalInput(int id)
	{
		super(id);
	}

	public void activate()
	{
		this.next = true;
		this.markDirty();
	}

	public void deactivate()
	{
		this.next = false;
		this.markDirty();
	}

	@Override
	public void clean()
	{
		this.prev = this.next;

		super.clean();
	}

	@Override
	public float getMotion()
	{
		return this.getAction() - this.getPreviousAction();
	}

	@Override
	public float getRange()
	{
		return this.getAction();
	}

	@Override
	public int getPreviousAction()
	{
		return this.prev ? 1 : -1;
	}

	@Override
	public int getAction()
	{
		return this.next ? 1 : -1;
	}

	@Override
	public boolean isStateChanged()
	{
		return this.prev != this.next;
	}

	@Override
	public boolean getState()
	{
		return this.next;
	}
}
