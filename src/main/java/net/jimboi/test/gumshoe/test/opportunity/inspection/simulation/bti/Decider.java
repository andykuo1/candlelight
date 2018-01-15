package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.bti;

/**
 * Created by Andy on 12/14/17.
 */
public abstract class Decider
{
	public String name;

	public Decider(String name)
	{
		this.name = name;
	}

	public abstract boolean decide(BehaviorTraverser traverser);

	public void activate(BehaviorTraverser traverser)
	{
	}

	public void deactivate(BehaviorTraverser traverser)
	{
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
