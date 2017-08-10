package org.bstone.window.input;

/**
 * Created by Andy on 5/4/17.
 */
public final class Input
{
	private float prevAmount;
	private float amount;
	private float nextAmount;

	private final int id;

	public Input(int id)
	{
		this.id = id;
	}

	public void update(float amount)
	{
		this.nextAmount = amount;
	}

	public void poll()
	{
		this.prevAmount = this.amount;
		this.amount = this.nextAmount;
	}

	public void consume()
	{
		this.amount = 0;
		this.nextAmount = 0;
	}

	public boolean isDown()
	{
		return this.amount > 0;
	}

	public boolean isPressed()
	{
		return this.prevAmount <= 0 && this.amount > 0;
	}

	public boolean isReleased()
	{
		return this.prevAmount > 0 && this.amount <= 0;
	}

	public float getAmount()
	{
		return this.amount;
	}

	public float getMotion()
	{
		return this.prevAmount - this.amount;
	}

	public int getID()
	{
		return this.id;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Input)
		{
			return ((Input) o).getID() == this.getID();
		}

		return false;
	}
}
