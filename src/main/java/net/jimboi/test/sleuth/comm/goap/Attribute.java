package net.jimboi.test.sleuth.comm.goap;

/**
 * Created by Andy on 9/29/17.
 */
public class Attribute
{
	public static Attribute create(String name)
	{
		return new Attribute(name);
	}

	public static SubAttribute[] create(String family, String... names)
	{
		SubAttribute[] result = new SubAttribute[names.length];
		for(int i = 0; i < names.length; ++i)
		{
			result[i] = new SubAttribute(names[i], family, i);
		}
		return result;
	}

	protected final String name;

	protected Attribute(String name)
	{
		this.name = name;
	}

	public String getID()
	{
		return this.name;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Attribute)
		{
			return this.getID().equals(((Attribute) o).getID());
		}
		return false;
	}

	@Override
	public String toString()
	{
		return this.getID();
	}
}
