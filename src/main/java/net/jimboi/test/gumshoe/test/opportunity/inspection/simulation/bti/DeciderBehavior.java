package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti;

/**
 * Created by Andy on 12/14/17.
 */
public class DeciderBehavior extends Decider
{
	public DeciderBehavior(String name)
	{
		super(name);
	}

	@Override
	public boolean decide(BehaviorTraverser traverser)
	{
		return true;
	}

	public boolean tick()
	{
		return true;
	}
}
