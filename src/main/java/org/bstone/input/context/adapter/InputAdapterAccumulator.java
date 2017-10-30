package org.bstone.input.context.adapter;

/**
 * Created by Andy on 10/30/17.
 */
public class InputAdapterAccumulator implements IRange
{
	private final IRange[] adapters;

	public InputAdapterAccumulator(IRange... adapters)
	{
		this.adapters = adapters;
	}

	@Override
	public Float poll()
	{
		float f = 0;
		for (IRange range : this.adapters)
		{
			Float g = range.poll();
			if (g != null)
			{
				f += g;
			}
		}
		return f;
	}
}
