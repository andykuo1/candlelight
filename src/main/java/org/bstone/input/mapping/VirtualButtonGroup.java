package org.bstone.input.mapping;

/**
 * Created by Andy on 10/28/17.
 */
public class VirtualButtonGroup extends ButtonInput implements VirtualInput
{
	private final Input[] inputs;

	public VirtualButtonGroup(int id, Input[] inputs)
	{
		super(id);

		this.inputs = inputs;
	}

	@Override
	public void update()
	{
		this.next = 0;
		for(Input input : this.inputs)
		{
			int action = input.getAction();
			if (action > this.next)
			{
				this.next = action;
			}
		}
		this.markDirty();
	}
}
