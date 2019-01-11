package canary.test.gumshoe.test.opportunity.inspection.simulation.bti;

import canary.test.gumshoe.test.opportunity.inspection.simulation.bti.policy.RandomIndexIterator;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by Andy on 12/19/17.
 */
public class DeciderGroupProbabilistic extends DeciderGroup
{
	public DeciderGroupProbabilistic(String name, Decider[] children)
	{
		super(name, children);
	}

	@Override
	public Iterator<Decider> getIterator(BehaviorTraverser traverser)
	{
		return new DeciderIterator(new RandomIndexIterator(this.children.length, new Random()), this.children);
	}
}
