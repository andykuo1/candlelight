package org.bstone.input.context;

/**
 * Created by Andy on 10/13/17.
 */
public class ButtonInput extends Input implements IAction
{
	protected int prev;
	protected int next;

	public ButtonInput(int id)
	{
		super(id);
	}

	public void press()
	{
		this.next = 1;
		this.markDirty();
	}

	public void release()
	{
		this.next = 0;
		this.markDirty();
	}

	@Override
	public void clean()
	{
		this.prev = this.next;

		super.clean();
	}

	@Override
	public int getPreviousAction()
	{
		return this.prev;
	}

	@Override
	public int getAction()
	{
		return this.next;
	}
}
