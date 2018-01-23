package org.zilar.in.adapter;

import org.zilar.in.InputState;
import org.zilar.in.provider.InputProvider;

/**
 * Created by Andy on 1/22/18.
 */
public class ButtonReleaseAdapter extends InputAdapter
{
	private final int[] buttons;

	public ButtonReleaseAdapter(InputProvider provider, int... buttons)
	{
		super(provider);
		this.buttons = buttons;
	}

	@Override
	public Boolean getState(InputState inputState)
	{
		for(int i = 0; i < this.buttons.length; ++i)
		{
			int id = this.buttons[i];
			Integer change = inputState.getButtonChange(id);
			if (change != null)
			{
				inputState.consumeButton(id);
				return change < 0;
			}
		}
		return null;
	}

	@Override
	public Integer getAction(InputState inputState)
	{
		for(int i = 0; i < this.buttons.length; ++i)
		{
			int id = this.buttons[i];
			Integer change = inputState.getButtonChange(id);
			if (change != null)
			{
				inputState.consumeButton(id);
				return -change;
			}
		}
		return null;
	}

	@Override
	public Float getRange(InputState inputState)
	{
		for(int i = 0; i < this.buttons.length; ++i)
		{
			int id = this.buttons[i];
			Integer change = inputState.getButtonChange(id);
			if (change != null)
			{
				inputState.consumeButton(id);
				return -change.floatValue();
			}
		}
		return null;
	}
}
