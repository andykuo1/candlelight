package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti.policy;

import java.util.Iterator;

/**
 * Created by Andy on 12/18/17.
 */
public abstract class IndexIterator implements Iterator<Integer>
{
	protected final int length;

	public IndexIterator(int length)
	{
		this.length = length;
	}
}
