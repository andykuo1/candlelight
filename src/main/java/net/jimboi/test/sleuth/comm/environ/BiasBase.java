package net.jimboi.test.sleuth.comm.environ;

import org.bstone.util.Pair;
import org.bstone.util.small.SmallMap;

import java.util.Map;

/**
 * Created by Andy on 9/27/17.
 */
public class BiasBase extends Bias
{
	private Map<Pair<String, Boolean>, Integer> biases = new SmallMap<>();

	public BiasBase addBias(String state, boolean enabled, int bias)
	{
		this.biases.put(new Pair<>(state, enabled), bias);
		return this;
	}

	@Override
	public int evaluate(Environment env, String state, boolean enabled)
	{
		return this.biases.getOrDefault(new Pair<>(state, enabled), 0);
	}
}
