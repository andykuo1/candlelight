package org.zilar.in.adapter.newadapt;

import org.zilar.in.InputState;
import org.zilar.in.adapter.InputStateAdapter;
import org.zilar.in.provider.ButtonInput;

/**
 * Created by Andy on 1/23/18.
 */
public class ButtonDownAdapter extends InputStateAdapter
{
	private final ButtonInput[] inputs;
	private final boolean multikey;

	public ButtonDownAdapter(ButtonInput...inputs)
	{
		this(false, inputs);
	}

	public ButtonDownAdapter(boolean multikey, ButtonInput...inputs)
	{
		this.multikey = multikey;
		this.inputs = inputs;
	}

	//TODO: Except for combos, should we consume all registered inputs?

	@Override
	public Boolean getState(InputState inputState)
	{
		for(ButtonInput input : this.inputs)
		{
			if (inputState.contains(input))
			{
				Integer button = inputState.getButton(input.getID());
				if (button != null && button > 0)
				{
					if (!this.multikey)
					{
						inputState.consumeButton(input.getID());
						return true;
					}
				}
				else if (this.multikey)
				{
					return false;
				}
			}
		}
		return this.multikey;
	}
}
