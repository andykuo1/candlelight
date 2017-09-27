package net.jimboi.test.sleuth.queso;

/**
 * Created by Andy on 9/26/17.
 */
public class AccionBase extends Accion
{
	private final String[] required;
	private final String[] resulted;
	private final int cost;

	public AccionBase(String[] required, String[] resulted, int cost)
	{
		this.required = required;
		this.resulted = resulted;
		this.cost = cost;
	}

	@Override
	public String[] getRequiredConditions()
	{
		return this.required;
	}

	@Override
	public String[] getResultedConditions()
	{
		return this.resulted;
	}

	@Override
	public int getCost()
	{
		return this.cost;
	}
}
