package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.goapi;

/**
 * Created by Andy on 12/9/17.
 */
public interface ActionableAgent
{
	Iterable<Action> getAvailableAction(Environment env);
}
