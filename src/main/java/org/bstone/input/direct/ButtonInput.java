package org.bstone.input.direct;

/**
 * Created by Andy on 10/29/17.
 */
public class ButtonInput extends AbstractInput
{
	public static final int RELEASED = -1;
	public static final int NONE = 0;
	public static final int PRESSED = 1;
	public static final int HOLD = 2;

	protected int prev;
	protected int next;

	public ButtonInput(int id)
	{
		super(id);
	}

	public void press()
	{
		if (this.next != HOLD) this.next = PRESSED;
		this.markDirty();
	}

	public void release()
	{
		if (this.next != NONE) this.next = RELEASED;
		this.markDirty();
	}

	@Override
	public void poll()
	{
		this.prev = this.next;

		if (this.next == PRESSED) this.next = HOLD;
		if (this.next == RELEASED) this.next = NONE;

		super.poll();
	}

	public boolean getState()
	{
		return this.next > 0;
	}

	public boolean isPressed()
	{
		return this.next == PRESSED;
	}

	public boolean isReleased()
	{
		return this.next == RELEASED;
	}

	public int getAction()
	{
		return this.next;
	}

	public int getPreviousAction()
	{
		return this.prev;
	}

	public boolean getActionChanged()
	{
		return this.next != this.prev;
	}
}
