package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.goapi;

/**
 * Created by Andy on 12/9/17.
 */
public interface Effect
{
	boolean apply(Environment env);

	default int getCost()
	{
		return 1;
	}
}
