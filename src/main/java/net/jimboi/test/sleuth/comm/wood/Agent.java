package net.jimboi.test.sleuth.comm.wood;

import net.jimboi.test.sleuth.comm.goap.Action;
import net.jimboi.test.sleuth.comm.goap.Environment;

/**
 * Created by Andy on 9/27/17.
 */
public abstract class Agent
{
	public abstract Iterable<Action> getPossibleActions(Environment env);
}
