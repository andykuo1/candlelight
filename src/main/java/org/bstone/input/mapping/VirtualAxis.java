package org.bstone.input.mapping;

/**
 * Created by Andy on 10/13/17.
 */
public class VirtualAxis extends AxisInput implements VirtualInput
{
	private Input positive;
	private Input negative;

	public VirtualAxis(int id, Input positive, Input negative)
	{
		super(id);

		this.positive = positive;
		this.negative = negative;
	}

	@Override
	public void update()
	{
		this.next = (this.positive.getState() ? 1 : 0) - (this.negative.getState() ? 1 : 0);
		this.markDirty();
	}
}
