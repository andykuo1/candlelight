package canary.test.sleuth.comm.wood;

import canary.test.sleuth.comm.goap.Action;
import canary.test.sleuth.comm.goap.Environment;

/**
 * Created by Andy on 9/27/17.
 */
public abstract class Agent
{
	public abstract Iterable<Action> getPossibleActions(Environment env);
}
