package net.jimboi.test.sleuth.comm.goap;

import java.util.Random;

/**
 * Created by Andy on 9/27/17.
 */
public interface Action
{
	boolean isSupportedConditions(Environment env);

	boolean applyEffects(MutableEnvironment env, Random rand);

	int evaluateCosts(Environment env);
}
