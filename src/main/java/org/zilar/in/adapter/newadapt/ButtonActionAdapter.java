package org.zilar.in.adapter.newadapt;

import org.zilar.in.InputState;
import org.zilar.in.adapter.InputActionAdapter;
import org.zilar.in.provider.ButtonInput;

/**
 * Created by Andy on 1/23/18.
 */
public class ButtonActionAdapter extends InputActionAdapter
{
	private final ButtonInput[] inputs;

	public ButtonActionAdapter(ButtonInput...inputs)
	{
		this.inputs = inputs;
	}

	@Override
	public Integer getAction(InputState inputState)
	{
		for(ButtonInput input : this.inputs)
		{
			if (inputState.contains(input))
			{
				Integer change = inputState.getButtonChange(input.getID());
				if (change != null && change > 0)
				{
					//TODO: this may skip a necessary press/release state, if pressed simultaneously...
					inputState.consumeButtonChange(input.getID());
					return change;
				}
			}
		}
		return null;
	}
}
