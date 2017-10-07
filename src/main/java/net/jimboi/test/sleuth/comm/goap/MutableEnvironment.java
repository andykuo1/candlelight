package net.jimboi.test.sleuth.comm.goap;

/**
 * Created by Andy on 9/27/17.
 */
public class MutableEnvironment implements Environment
{
	private final AttributeSet attributes = new AttributeSet();

	public MutableEnvironment set(Environment env)
	{
		for(Attribute attribute : env.getAttributes())
		{
			this.attributes.add(attribute);
		}
		return this;
	}

	public MutableEnvironment set(Attribute attribute, boolean result)
	{
		if (result)
		{
			this.addAttribute(attribute);
		}
		else
		{
			this.removeAttribute(attribute);
		}

		return this;
	}

	public boolean addAttribute(Attribute attribute)
	{
		return this.attributes.add(attribute);
	}

	public boolean removeAttribute(Attribute attribute)
	{
		return this.attributes.remove(attribute);
	}

	public boolean removeAttributeFamily(String family)
	{
		return this.attributes.remove(family);
	}

	@Override
	public boolean containsAttribute(Attribute attribute)
	{
		return this.attributes.contains(attribute);
	}

	@Override
	public Iterable<Attribute> getAttributes()
	{
		return this.attributes;
	}

	@Override
	public String toString()
	{
		return super.toString() + ":" + this.attributes.toString();
	}
}
