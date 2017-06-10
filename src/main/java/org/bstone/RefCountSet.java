package org.bstone;

import org.bstone.util.small.SmallSet;

/**
 * Created by Andy on 6/10/17.
 */
public class RefCountSet<T> extends SmallSet<T>
{
	public static final boolean VERBOSE = false;

	@Override
	public boolean add(T t)
	{
		if (VERBOSE)
		{
			System.out.println("CREATE : " + t.getClass().getSimpleName() + " (" + this.size() + ") = " + t);
		}

		return super.add(t);
	}

	@Override
	public boolean remove(Object value)
	{
		boolean flag = super.remove(value);
		if (!flag)
		{
			System.err.println("DESTROY : " + value.getClass().getSimpleName() + " (" + this.size() + ") = " + value + " - Could not be removed! It probably was removed too many times!");
			new Exception().printStackTrace();
		}
		else
		{
			if (VERBOSE)
			{
				System.out.println("DESTROY : " + value.getClass().getSimpleName() + " (" + this.size() + ") = " + value);
			}

			if (this.isEmpty())
			{
				System.out.println("CLEARED : " + value.getClass().getSimpleName());
			}
		}
		return flag;
	}
}
