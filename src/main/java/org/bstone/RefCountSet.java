package org.bstone;

import org.bstone.util.small.SmallSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andy on 6/10/17.
 */
public class RefCountSet<T> extends SmallSet<T>
{
	private static final Logger LOG = LoggerFactory.getLogger(RefCountSet.class);
	private static int TOTAL_REF_COUNT_SET;

	private final String name;

	public RefCountSet(String name)
	{
		this.name = name;
	}

	@Override
	public boolean add(T t)
	{
		if (this.isEmpty())
		{
			++TOTAL_REF_COUNT_SET;
		}

		LOG.debug("Created ref-counted resource: " + this.name + "(" + this.size() + ") = " + t);

		return super.add(t);
	}

	@Override
	public boolean remove(Object value)
	{
		boolean flag = super.remove(value);
		if (!flag)
		{
			System.err.println("Destroyed ref-counted resource: " + this.name + "(" + this.size() + ") = " + value + " - Could not be removed! It probably was removed too many times!");
			new Exception().printStackTrace();
		}
		else
		{
			LOG.debug("Destroyed ref-counted resource: " + this.name + "(" + this.size() + ") = " + value);

			if (this.isEmpty())
			{
				LOG.debug("PASS: " + this.name + "(s) is destroyed!");
				--TOTAL_REF_COUNT_SET;

				if (TOTAL_REF_COUNT_SET == 0)
				{
					LOG.warn("SUCCESS: All ref-counted resources are cleared!");
				}
			}
		}
		return flag;
	}
}
