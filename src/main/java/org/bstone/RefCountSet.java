package org.bstone;

import org.bstone.util.small.SmallSet;
import org.qsilver.poma.Poma;

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

		Poma.debug("Created ref-counted resource: " + t.getClass().getSimpleName() + "(" + this.size() + ") = " + t);

		return super.add(t);
	}

	@Override
	public boolean remove(Object value)
	{
		boolean flag = super.remove(value);
		if (!flag)
		{
			System.err.println("Destroyed ref-counted resource: " + value.getClass().getSimpleName() + "(" + this.size() + ") = " + value + " - Could not be removed! It probably was removed too many times!");
			new Exception().printStackTrace();
		}
		else
		{
			Poma.debug("Destroyed ref-counted resource: " + value.getClass().getSimpleName() + "(" + this.size() + ") = " + value);

			if (this.isEmpty())
			{
				System.out.println("PASS: " + value.getClass().getSimpleName() + "(s) is destroyed!");
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
