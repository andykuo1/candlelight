package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti;

import java.util.Iterator;

/**
 * Created by Andy on 12/19/17.
 */
public final class DeciderIterator implements Iterator<Decider>
{
	private final Iterator<Integer> indices;
	private final Decider[] deciders;

	public DeciderIterator(Iterator<Integer> indices, Decider[] deciders)
	{
		this.indices = indices;
		this.deciders = deciders;
	}

	@Override
	public boolean hasNext()
	{
		return this.indices.hasNext();
	}

	@Override
	public Decider next()
	{
		return this.deciders[this.indices.next()];
	}
}
