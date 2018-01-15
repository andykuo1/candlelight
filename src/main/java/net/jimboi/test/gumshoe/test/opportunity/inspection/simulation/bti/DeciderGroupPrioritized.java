package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti;

import net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti.policy.SequentialIndexIterator;

import java.util.Iterator;

/**
 * Created by Andy on 12/19/17.
 */
public class DeciderGroupPrioritized extends DeciderGroup
{
	public DeciderGroupPrioritized(String name, Decider[] children)
	{
		super(name, children);
	}

	@Override
	public Iterator<Decider> getIterator(BehaviorTraverser traverser)
	{
		return new DeciderIterator(new SequentialIndexIterator(this.children.length), this.children);
	}
}
