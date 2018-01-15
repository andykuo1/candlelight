package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti.policy;

/**
 * Created by Andy on 12/18/17.
 */
public final class SequentialIndexIterator extends IndexIterator
{
	protected int index;

	public SequentialIndexIterator(int length)
	{
		this(length, 0);
	}

	public SequentialIndexIterator(int length, int startIndex)
	{
		super(length);
		this.index = startIndex;
	}

	@Override
	public boolean hasNext()
	{
		return this.index < this.length;
	}

	@Override
	public Integer next()
	{
		return this.index++;
	}
}
