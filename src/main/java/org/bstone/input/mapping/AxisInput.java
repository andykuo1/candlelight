package org.bstone.input.mapping;

/**
 * Created by Andy on 10/13/17.
 */
public class AxisInput extends Input
{
	protected float prev;
	protected float next;

	public AxisInput(int id)
	{
		super(id);
	}

	public void move(float amount)
	{
		this.next += amount;
		this.markDirty();
	}

	public void set(float amount)
	{
		this.next = amount;
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
		return (int) Math.ceil(this.prev);
	}

	@Override
	public int getAction()
	{
		return (int) Math.ceil(this.next);
	}
}
