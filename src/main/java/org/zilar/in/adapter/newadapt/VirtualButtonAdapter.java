package org.zilar.in.adapter.newadapt;

import org.zilar.in.InputState;
import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.adapter.InputStateAdapter;

/**
 * Created by Andy on 1/23/18.
 */
public class VirtualButtonAdapter extends InputStateAdapter
{
	private final InputRangeAdapter range;

	public VirtualButtonAdapter(InputRangeAdapter range)
	{
		this.range = range;
	}

	@Override
	public Boolean getState(InputState inputState)
	{
		Float range = this.range.getRange(inputState);
		if (range != null)
		{
			return range != 0;
		}
		return null;
	}
}
