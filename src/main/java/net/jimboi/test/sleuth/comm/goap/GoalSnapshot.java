package net.jimboi.test.sleuth.comm.goap;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 9/30/17.
 */
public class GoalSnapshot extends MutableEnvironment
{
	private final Set<Condition> conditions = new HashSet<>();

	@Override
	public GoalSnapshot set(Environment env)
	{
		return (GoalSnapshot) super.set(env);
	}

	@Override
	public GoalSnapshot set(Attribute attribute, boolean result)
	{
		return (GoalSnapshot) super.set(attribute, result);
	}

	public GoalSnapshot set(Condition condition)
	{
		this.conditions.add(condition);
		return this;
	}

	@Override
	public int getEstimatedRemainingActionCost(Environment env)
	{
		int cost = super.getEstimatedRemainingActionCost(env);
		for(Condition condition : this.conditions)
		{
			if (!condition.test(env))
			{
				cost++;
			}
		}
		return cost;
	}

	@Override
	public boolean isSatisfiedActionState(Environment env)
	{
		if (super.isSatisfiedActionState(env))
		{
			for(Condition condition : this.conditions)
			{
				if (!condition.test(env))
				{
					return false;
				}
			}

			return true;
		}

		return false;
	}
}
