package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.goapi;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 12/9/17.
 */
public class Action
{
	private final String name;
	private final int cost;

	private final List<Condition> conditions;
	private final List<Effect> effects;

	public Action(String name, int cost)
	{
		this.name = name;
		this.cost = cost;

		this.conditions = new LinkedList<>();
		this.effects = new LinkedList<>();
	}

	public Action addCondition(String id, boolean result)
	{
		return this.addCondition(new ActionState(id, result));
	}

	public Action addCondition(Condition condition)
	{
		this.conditions.add(condition);
		return this;
	}

	public Action addEffect(String id, boolean result)
	{
		return this.addEffect(new ActionState(id, result));
	}

	public Action addEffect(Effect effect)
	{
		this.effects.add(effect);
		return this;
	}

	public boolean isConditionSatisfied(Environment env)
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

	public boolean applyEffects(Environment env)
	{
		boolean flag = false;
		for(Effect effect : this.effects)
		{
			if (effect.apply(env))
			{
				flag = true;
			}
		}
		return flag;
	}

	public int evaluateCost(Environment env, ActionableAgent agent)
	{
		int i = this.cost;
		for(Effect effect : this.effects)
		{
			i += effect.getCost();
		}
		return i;
	}

	public final Iterable<Condition> getConditions()
	{
		return this.conditions;
	}

	public final Iterable<Effect> getEffects()
	{
		return this.effects;
	}

	public final String getName()
	{
		return this.name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
