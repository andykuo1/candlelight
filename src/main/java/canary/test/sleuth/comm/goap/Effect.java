package canary.test.sleuth.comm.goap;

import java.util.Random;

/**
 * Created by Andy on 9/30/17.
 */
@FunctionalInterface
public interface Effect
{
	default int evaluate(Environment env)
	{
		return 1;
	}

	boolean apply(MutableEnvironment env, Random rand);
}
