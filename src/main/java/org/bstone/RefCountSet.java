package org.bstone;

import org.bstone.util.small.SmallSet;
import org.qsilver.poma.Poma;
import org.qsilver.service.Service;

/**
 * Created by Andy on 6/10/17.
 */
public class RefCountSet<T> extends SmallSet<T>
{
	private static int TOTAL_REF_COUNT_SET;

	@Override
	public boolean add(T t)
	{
		if (this.isEmpty())
		{
			++TOTAL_REF_COUNT_SET;
		}

		String className = t instanceof Service ? "Service" : t.getClass().getSimpleName();
		Poma.debug("Created ref-counted resource: " + className + "(" + this.size() + ") = " + t);

		return super.add(t);
	}

	@Override
	public boolean remove(Object value)
	{
		boolean flag = super.remove(value);
		if (!flag)
		{
			String className = value instanceof Service ? "Service" : value.getClass().getSimpleName();
			System.err.println("Destroyed ref-counted resource: " + className + "(" + this.size() + ") = " + value + " - Could not be removed! It probably was removed too many times!");
			new Exception().printStackTrace();
		}
		else
		{
			String className = value instanceof Service ? "Service" : value.getClass().getSimpleName();
			Poma.debug("Destroyed ref-counted resource: " + className + "(" + this.size() + ") = " + value);

			if (this.isEmpty())
			{
				System.out.println("PASS: " + className + "(s) is destroyed!");
				--TOTAL_REF_COUNT_SET;

				if (TOTAL_REF_COUNT_SET == 0)
				{
					Poma.warn("SUCCESS: All ref-counted resources are cleared!");
				}
			}
		}
		return flag;
	}
}
