package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti.policy;

/**
 * Created by Andy on 12/18/17.
 */
public class FixedIndexIterator extends IndexIterator
{
	protected final int[] indices;
	protected int index;

	public FixedIndexIterator(int[] array)
	{
		super(array.length);

		this.indices = new int[this.length];

		for(int i = 0; i < this.indices.length && i < array.length; ++i)
		{
			this.indices[i] = array[i];
		}
	}

	protected FixedIndexIterator(int length)
	{
		super(length);

		this.indices = new int[this.length];
	}

	@Override
	public final boolean hasNext()
	{
		return this.index < this.indices.length;
	}

	@Override
	public final Integer next()
	{
		return this.indices[this.index++];
	}
}
