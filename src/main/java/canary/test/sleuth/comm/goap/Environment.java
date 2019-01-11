package canary.test.sleuth.comm.goap;

/**
 * Created by Andy on 9/27/17.
 */
public interface Environment
{
	default int getEstimatedRemainingActionCost(Environment env)
	{
		int diff = 0;
		for(Attribute attribute : this.getAttributes())
		{
			if (!env.containsAttribute(attribute))
			{
				diff++;
			}
		}
		return diff;
	}

	default boolean isSatisfiedActionState(Environment env)
	{
		for(Attribute attribute : this.getAttributes())
		{
			if (!env.containsAttribute(attribute))
			{
				return false;
			}
		}
		return true;
	}

	default SubAttribute findAttributeFamily(String family)
	{
		for(Attribute attribute : this.getAttributes())
		{
			if (attribute instanceof SubAttribute)
			{
				SubAttribute subAttribute = (SubAttribute) attribute;
				if (family.equals(subAttribute.getFamily()))
				{
					return subAttribute;
				}
			}
		}

		return null;
	}

	boolean containsAttribute(Attribute attribute);

	Iterable<Attribute> getAttributes();
}
