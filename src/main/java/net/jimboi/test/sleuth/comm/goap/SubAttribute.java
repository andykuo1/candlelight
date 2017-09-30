package net.jimboi.test.sleuth.comm.goap;

/**
 * Created by Andy on 9/30/17.
 */
public class SubAttribute extends Attribute
{
	protected final String family;
	protected final int ordinal;

	protected SubAttribute(String name, String family, int ordinal)
	{
		super(name);

		this.family = family;
		this.ordinal = ordinal;
	}

	public String getName()
	{
		return this.name;
	}

	public String getFamily()
	{
		return this.family;
	}

	public int ordinal()
	{
		return this.ordinal;
	}

	@Override
	public String getID()
	{
		return this.family + "_" + super.getID();
	}
}
