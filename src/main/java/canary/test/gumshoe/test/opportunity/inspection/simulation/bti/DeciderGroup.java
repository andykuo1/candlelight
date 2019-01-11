package canary.test.gumshoe.test.opportunity.inspection.simulation.bti;

import java.util.Iterator;

/**
 * Created by Andy on 12/14/17.
 */
public abstract class DeciderGroup extends Decider
{
	protected final Decider[] children;

	public DeciderGroup(String name, Decider[] children)
	{
		super(name);

		this.children = children;
	}

	@Override
	public boolean decide(BehaviorTraverser traverser)
	{
		return true;
	}

	public final Decider[] getChildren()
	{
		return this.children;
	}

	public abstract Iterator<Decider> getIterator(BehaviorTraverser traverser);
}
