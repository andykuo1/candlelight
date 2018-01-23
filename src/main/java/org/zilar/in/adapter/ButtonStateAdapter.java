package org.zilar.in.adapter;

import org.zilar.in.InputProvider;
import org.zilar.in.InputState;

/**
 * Created by Andy on 1/22/18.
 */
public class ButtonStateAdapter extends InputAdapter
{
	private final int[] buttons;

	public ButtonStateAdapter(String name, InputProvider provider, int... buttons)
	{
		super(name, provider);
		this.buttons = buttons;
	}

	@Override
	public Boolean getState(InputState inputState)
	{
		for(int i = 0; i < this.buttons.length; ++i)
		{
			int id = this.buttons[i];
			Integer button = inputState.getButton(id);
			if (button != null)
			{
				inputState.consumeButton(id);
				return button != 0;
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
			Integer button = inputState.getButton(id);
			if (button != null)
			{
				inputState.consumeButton(id);
				return button;
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
			Integer button = inputState.getButton(id);
			if (button != null)
			{
				inputState.consumeButton(id);
				return button.floatValue();
			}
		}
		return null;
	}
}
