package org.bstone.input.context;

/**
 * Created by Andy on 10/13/17.
 */
public class ConditionalInput extends Input implements IState
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
