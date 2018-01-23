package org.zilar.in.adapter.newadapt;

import org.zilar.in.InputState;
import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.adapter.InputStateAdapter;

/**
 * Created by Andy on 1/23/18.
 */
public class VirtualAxisAdapter extends InputRangeAdapter
{
	private final InputStateAdapter pos;
	private final InputStateAdapter neg;

	public VirtualAxisAdapter(InputStateAdapter pos, InputStateAdapter neg)
	{
		this.pos = pos;
		this.neg = neg;
	}

	@Override
	public Float getRange(InputState inputState)
	{
		Boolean positive = this.pos.getState(inputState);
		Boolean negative = this.neg.getState(inputState);

		Float f = null;
		if (positive != null && positive) f = 1F;
		if (negative != null && negative) f = f == null ? -1 : f - 1F;
		return f;
	}
}
