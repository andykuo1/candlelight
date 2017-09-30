package net.jimboi.test.sleuth.comm.goap;

import java.util.Random;

/**
 * Created by Andy on 9/30/17.
 */
public class EffectBase implements Effect
{
	private final Attribute attribute;
	private final boolean result;
	private final float probability;

	public EffectBase(Attribute attribute, boolean result)
	{
		this(attribute, result, 1);
	}

	public EffectBase(Attribute attribute, boolean result, float probability)
	{
		this.attribute = attribute;
		this.result = result;
		this.probability = probability;
	}

	@Override
	public boolean apply(MutableEnvironment env, Random rand)
	{
		boolean flag = true;
		if (rand != null && this.probability < 1)
		{
			flag = rand.nextFloat() < this.probability;
		}

		if (flag)
		{
			if (this.result)
			{
				return env.addAttribute(this.attribute);
			}
			else
			{
				return env.removeAttribute(this.attribute);
			}
		}

		return false;
	}
}
