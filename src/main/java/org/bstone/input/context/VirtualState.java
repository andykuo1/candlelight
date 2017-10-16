package org.bstone.input.context;

/**
 * Created by Andy on 10/13/17.
 */
public class VirtualState extends ConditionalInput implements IVirtual
{
	private ButtonInput input;

	public VirtualState(int id, ButtonInput input)
	{
		super(id);
		this.input = input;
	}

	@Override
	public void update()
	{
		this.next = this.input.getAction() != 0;
	}
}
