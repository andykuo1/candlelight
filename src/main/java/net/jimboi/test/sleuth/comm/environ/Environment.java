package net.jimboi.test.sleuth.comm.environ;

/**
 * Created by Andy on 9/27/17.
 */
public abstract class Environment
{
	public abstract FiniteEnvironment setup(FiniteEnvironment dst);

	public abstract boolean contains(String state, boolean enabled);
}
