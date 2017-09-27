package net.jimboi.test.sleuth.queso;

/**
 * Created by Andy on 9/26/17.
 */
public class GolBase extends Gol
{
	private final String[] conditions;

	public GolBase(String... conditions)
	{
		this.conditions = conditions;
	}

	@Override
	public String[] getConditions()
	{
		return this.conditions;
	}
}
