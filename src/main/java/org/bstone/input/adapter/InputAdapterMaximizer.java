package org.bstone.input.adapter;

/**
 * Created by Andy on 10/30/17.
 */
public class InputAdapterMaximizer<T extends Comparable<T>> implements InputAdapter<T>
{
	private final InputAdapter<T>[] adapters;

	public InputAdapterMaximizer(InputAdapter<T>... adapters)
	{
		this.adapters = adapters;
	}

	@Override
	public T poll()
	{
		T result = null;
		for (InputAdapter<T> adapter : this.adapters)
		{
			T t = adapter.poll();
			if (t != null)
			{
				if (result == null || t.compareTo(result) > 0)
				{
					result = t;
				}
				else
				{
					return result;
				}
			}
		}
		return result;
	}
}
