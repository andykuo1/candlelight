package net.jimboi.test.sleuth.comm.wood;

import net.jimboi.test.sleuth.comm.goap.Action;
import net.jimboi.test.sleuth.comm.goap.Environment;

/**
 * Created by Andy on 9/27/17.
 */
public class AgentBase extends Agent
{
	private final Iterable<Action> actions;

	public AgentBase(Iterable<Action> actions)
	{
		this.actions = actions;
	}

	@Override
	public Iterable<Action> getPossibleActions(Environment env)
	{
		return this.actions;
	}
}
