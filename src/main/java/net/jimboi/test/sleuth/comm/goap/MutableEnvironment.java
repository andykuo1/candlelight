package net.jimboi.test.sleuth.comm.goap;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 9/27/17.
 */
public class MutableEnvironment implements Environment
{
	private final Set<Attribute> attributes = new HashSet<>();

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
		if (attribute instanceof SubAttribute)
		{
			SubAttribute subAttribute = (SubAttribute) attribute;
			SubAttribute other = null;
			for(Attribute attrib : this.attributes)
			{
				if (attrib instanceof SubAttribute)
				{
					other = (SubAttribute) attrib;
					if (subAttribute.getFamily().equals(other.getFamily()))
					{
						if (subAttribute.ordinal() != other.ordinal())
						{
							break;
						}
						else
						{
							return false;
						}
					}
				}
				other = null;
			}

			if (other != null)
			{
				this.attributes.remove(other);
			}

			return this.attributes.add(subAttribute);
		}
		else
		{
			return this.attributes.add(attribute);
		}
	}

	public boolean removeAttribute(Attribute attribute)
	{
		return this.attributes.remove(attribute);
	}

	public boolean removeAttributeFamily(String family)
	{
		Attribute target = null;
		for(Attribute attribute : this.attributes)
		{
			if (attribute instanceof SubAttribute)
			{
				SubAttribute subAttribute = (SubAttribute) attribute;
				if (family.equals(subAttribute.getFamily()))
				{
					target = subAttribute;
					break;
				}
			}
		}

		if (target != null)
		{
			this.attributes.remove(target);
			return true;
		}

		return false;
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
