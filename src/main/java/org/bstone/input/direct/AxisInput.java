package org.bstone.input.direct;

/**
 * Created by Andy on 10/29/17.
 */
public class AxisInput extends AbstractInput
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
	public void poll()
	{
		this.prev = this.next;

		super.poll();
	}

	public float getRange()
	{
		return this.next;
	}

	public float getMotion()
	{
		return this.next - this.prev;
	}
}
