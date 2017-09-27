package net.jimboi.test.sleuth.comm.environ;

import org.bstone.util.small.SmallMap;

import java.util.Map;

/**
 * Created by Andy on 9/27/17.
 */
public class GoalBase extends Goal
{
	private final Map<String, Boolean> states = new SmallMap<>();

	public GoalBase addGoal(String state, boolean enabled)
	{
		this.states.put(state, enabled);
		return this;
	}

	@Override
	public boolean contains(String state, boolean enabled)
	{
		return this.states.getOrDefault(state, false).equals(enabled);
	}

	@Override
	public FiniteEnvironment setSatisfied(FiniteEnvironment dst)
	{
		for(String state : this.states.keySet())
		{
			dst.add(state, this.states.get(state));
		}
		return dst;
	}

	@Override
	public boolean isSatisfied(Environment env)
	{
		int work = this.getRemainingWork(env);
		return work == 0;
	}

	@Override
	public int getRemainingWork(Environment env)
	{
		int diff = 0;
		for(Map.Entry<String, Boolean> entry : this.states.entrySet())
		{
			if (!env.contains(entry.getKey(), entry.getValue()))
			{
				diff++;
			}
		}
		return diff;
	}
}

