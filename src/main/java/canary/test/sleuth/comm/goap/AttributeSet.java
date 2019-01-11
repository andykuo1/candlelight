package canary.test.sleuth.comm.goap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 9/30/17.
 */
public class AttributeSet implements Iterable<Attribute>
{
	private Set<Attribute> attributes = new HashSet<>();

	public boolean add(Attribute attribute)
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

	public boolean remove(Attribute attribute)
	{
		return this.attributes.remove(attribute);
	}

	public boolean remove(String family)
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

	public boolean contains(Attribute attribute)
	{
		return this.attributes.contains(attribute);
	}

	public int size()
	{
		return this.attributes.size();
	}

	@Override
	public Iterator<Attribute> iterator()
	{
		return this.attributes.iterator();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof AttributeSet)
		{
			return this.attributes.equals(((AttributeSet) o).attributes);
		}

		return false;
	}

	@Override
	public String toString()
	{
		return this.attributes.toString();
	}
}
