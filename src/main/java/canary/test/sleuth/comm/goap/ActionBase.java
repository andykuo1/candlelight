package canary.test.sleuth.comm.goap;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andy on 9/27/17.
 */
public class ActionBase implements Action
{
	private final String name;
	private final Set<Effect> effects = new HashSet<>();
	private final Set<Condition> requirements = new HashSet<>();
	private final int baseCost;

	public ActionBase(String name, int baseCost)
	{
		this.name = name;
		this.baseCost = baseCost;
	}

	public ActionBase addRequirement(Attribute attribute, boolean result)
	{
		return this.addRequirement(new ConditionBase(attribute, result));
	}

	public ActionBase addRequirement(Attribute attribute)
	{
		return this.addRequirement(attribute, true);
	}

	public ActionBase addRequirement(Condition condition)
	{
		this.requirements.add(condition);
		return this;
	}

	public ActionBase addEffect(Attribute attribute, boolean result, float probability)
	{
		return this.addEffect(new EffectBase(attribute, result, probability));
	}

	public ActionBase addEffect(Attribute attribute, boolean result)
	{
		return this.addEffect(new EffectBase(attribute, result));
	}

	public ActionBase addEffect(Attribute attribute)
	{
		return this.addEffect(attribute, true);
	}

	public ActionBase addEffect(Effect effect)
	{
		this.effects.add(effect);
		return this;
	}

	@Override
	public boolean isSupportedConditions(Environment env)
	{
		for(Condition condition : this.requirements)
		{
			if (!condition.test(env)) return false;
		}
		return true;
	}

	@Override
	public boolean applyEffects(MutableEnvironment env, Random rand)
	{
		boolean flag = false;
		for(Effect effect : this.effects)
		{
			flag |= effect.apply(env, rand);
		}
		return flag;
	}

	@Override
	public int evaluateCosts(Environment env)
	{
		int total = this.baseCost;

		for(Effect effect : this.effects)
		{
			total += effect.evaluate(env);
		}

		return total;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
