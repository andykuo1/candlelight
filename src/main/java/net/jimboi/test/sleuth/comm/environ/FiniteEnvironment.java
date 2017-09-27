package net.jimboi.test.sleuth.comm.environ;

import org.bstone.util.small.SmallMap;

import java.util.Map;

/**
 * Created by Andy on 9/27/17.
 */
public class FiniteEnvironment extends Environment
{
	protected final Map<String, Boolean> states = new SmallMap<>();

	public void add(String state, boolean enabled)
	{
		this.states.put(state, enabled);
	}

	@Override
	public FiniteEnvironment setup(FiniteEnvironment dst)
	{
		for(String state : this.getStates())
		{
			dst.add(state, this.getValue(state));
		}
		return dst;
	}

	@Override
	public boolean contains(String state, boolean enabled)
	{
		return this.states.getOrDefault(state, false).equals(enabled);
	}

	public Boolean getValue(String state)
	{
		return this.states.get(state);
	}

	public Iterable<String> getStates()
	{
		return this.states.keySet();
	}
}
