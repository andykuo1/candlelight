package org.bstone.input.context;

/**
 * Created by Andy on 10/13/17.
 */
public class VirtualAxis extends AxisInput implements IVirtual
{
	private IState positive;
	private IState negative;

	public VirtualAxis(int id, IState positive, IState negative)
	{
		super(id);

		this.positive = positive;
		this.negative = negative;
	}

	@Override
	public void update()
	{
		this.next = (this.positive.getState() ? 1 : 0) - (this.negative.getState() ? 1 : 0);
	}
}
