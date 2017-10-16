package org.bstone.input.context;

/**
 * Created by Andy on 10/13/17.
 */
public class VirtualButton extends ButtonInput implements IVirtual
{
	private final AxisInput input;

	public VirtualButton(int id, AxisInput input)
	{
		super(id);

		this.input = input;
	}

	@Override
	public void update()
	{
		this.next = this.input.getRange() != 0 ? 1 : 0;
	}
}
