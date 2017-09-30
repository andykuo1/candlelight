package net.jimboi.test.sleuth.comm.goap;

/**
 * Created by Andy on 9/30/17.
 */
public class ConditionBase implements Condition
{
	private final Attribute attribute;
	private final boolean result;

	public ConditionBase(Attribute attribute, boolean result)
	{
		this.attribute = attribute;
		this.result = result;
	}

	@Override
	public boolean test(Environment env)
	{
		return env.containsAttribute(this.attribute) == this.result;
	}
}
