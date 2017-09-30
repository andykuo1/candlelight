package net.jimboi.test.sleuth.comm.goap;

/**
 * Created by Andy on 9/30/17.
 */
@FunctionalInterface
public interface Condition
{
	boolean test(Environment env);
}
