package org.zilar.in.adapter.newadapt;

import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.adapter.InputStateAdapter;
import org.zilar.in.state.InputState;

/**
 * Created by Andy on 1/23/18.
 */
public class VirtualAxisAdapter implements InputRangeAdapter
{
	private final InputStateAdapter pos;
	private final InputStateAdapter neg;

	private float range;

	public VirtualAxisAdapter(InputStateAdapter pos, InputStateAdapter neg)
	{
		this.pos = pos;
		this.neg = neg;
	}

	@Override
	public void update(InputState inputState)
	{
		boolean positive = this.pos.getState();
		boolean negative = this.neg.getState();

		if (positive) this.range = 1F;
		if (negative) this.range -= 1F;
	}

	@Override
	public float getRange()
	{
		return this.range;
	}
}
