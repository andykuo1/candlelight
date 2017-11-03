package org.bstone.input.direct;

/**
 * Created by Andy on 10/29/17.
 */
public class ButtonInput extends AbstractInput
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
	public void poll()
	{
		this.prev = this.next;

		super.poll();
	}

	public boolean getState()
	{
		return this.next == 1;
	}

	public boolean isPressed()
	{
		return this.prev != this.next && this.next == 1;
	}

	public boolean isReleased()
	{
		return this.prev != this.next && this.next == 0;
	}

	public int getAction()
	{
		return this.next;
	}

	public int getPreviousAction()
	{
		return this.prev;
	}
}
