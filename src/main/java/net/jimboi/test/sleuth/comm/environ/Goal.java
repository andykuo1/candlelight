package net.jimboi.test.sleuth.comm.environ;

/**
 * Created by Andy on 9/27/17.
 */
public abstract class Goal
{
	public abstract boolean contains(String state, boolean enabled);

	public abstract FiniteEnvironment setSatisfied(FiniteEnvironment dst);

	public abstract boolean isSatisfied(Environment env);

	public abstract int getRemainingWork(Environment env);
}
