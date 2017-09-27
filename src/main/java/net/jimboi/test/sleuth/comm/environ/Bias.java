package net.jimboi.test.sleuth.comm.environ;

/**
 * Created by Andy on 9/27/17.
 */
public abstract class Bias
{
	public abstract int evaluate(Environment env, String state, boolean enabled);
}
