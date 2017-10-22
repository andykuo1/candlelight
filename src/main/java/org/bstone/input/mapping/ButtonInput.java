package org.bstone.input.mapping;

/**
 * Created by Andy on 10/13/17.
 */
public class ButtonInput extends Input
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
	public float getMotion()
	{
		return this.next - this.prev;
	}

	@Override
	public float getRange()
	{
		return this.next;
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
