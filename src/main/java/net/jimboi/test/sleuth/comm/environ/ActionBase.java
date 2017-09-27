package net.jimboi.test.sleuth.comm.environ;

import net.jimboi.test.sleuth.comm.Agent;

import org.bstone.util.small.SmallMap;

import java.util.Map;
import java.util.Random;

/**
 * Created by Andy on 9/27/17.
 */
public class ActionBase extends Action
{
	private final String name;
	private final Map<String, Boolean> effects = new SmallMap<>();
	private final Map<String, Float> probability = new SmallMap<>();
	private final Map<String, Boolean> requirements = new SmallMap<>();
	private final int cost;

	public ActionBase(String name, int cost)
	{
		this.name = name;
		this.cost = cost;
	}

	public ActionBase addRequirement(String state, boolean enabled)
	{
		this.requirements.put(state, enabled);
		return this;
	}

	public ActionBase addEffect(String state, boolean enabled)
	{
		this.effects.put(state, enabled);
		//this.addRequirement(state, !enabled);
		return this;
	}

	public ActionBase addEffect(String state, boolean enabled, float probability)
	{
		this.effects.put(state, enabled);
		this.probability.put(state, probability);
		//if (probability > 0.5F) this.addRequirement(state, !enabled);
		return this;
	}

	@Override
	public boolean hasRequiredState(Agent user, Environment env)
	{
		for(String state : this.requirements.keySet())
		{
			if (!env.contains(state, this.requirements.get(state)))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public void predict(FiniteEnvironment env)
	{
		for(String state : this.effects.keySet())
		{
			float f = this.probability.getOrDefault(state, 1F);
			if (f > 0.5F)
			{
				env.add(state, this.effects.get(state));
			}
		}
	}

	@Override
	public void apply(FiniteEnvironment env, Random rand)
	{
		for(String state : this.effects.keySet())
		{
			float f = this.probability.getOrDefault(state, 1F);
			if (f >= 1 || rand.nextFloat() <= f)
			{
				env.add(state, this.effects.get(state));
			}
		}
	}

	@Override
	public int evaluate(Agent user, Environment env)
	{
		Iterable<Bias> biases = user.getPossibleBiases(env);
		int total = this.cost;
		for(String state : this.effects.keySet())
		{
			int cost = 0;
			for(Bias bias : biases)
			{
				cost += bias.evaluate(env, state, this.effects.get(state));
			}
			cost *= this.probability.getOrDefault(state, 1F);
			total += cost;
		}
		return total;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
