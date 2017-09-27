package net.jimboi.test.sleuth.comm.environ;

import net.jimboi.test.sleuth.comm.Agent;

import java.util.Random;

/**
 * Created by Andy on 9/27/17.
 */
public abstract class Action
{
	public abstract boolean hasRequiredState(Agent user, Environment env);

	public abstract void predict(FiniteEnvironment env);

	public abstract void apply(FiniteEnvironment env, Random rand);

	public abstract int evaluate(Agent user, Environment env);
}
