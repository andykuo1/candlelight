package org.zilar.in.adapter.newadapt;

import org.zilar.in.adapter.InputActionAdapter;
import org.zilar.in.adapter.InputStateAdapter;
import org.zilar.in.input.ButtonInput;
import org.zilar.in.state.InputState;

/**
 * Created by Andy on 1/23/18.
 */
public class ButtonAdapter implements InputActionAdapter, InputStateAdapter
{
	private final ButtonInput[] inputs;

	private int holds = 0;
	private int motion = 0;

	public ButtonAdapter(ButtonInput...inputs)
	{
		this.inputs = inputs;
	}

	@Override
	public void update(InputState inputState)
	{
		System.out.println("UPDATE");
		this.motion = 0;
		for(ButtonInput input : this.inputs)
		{
			if (inputState.contains(input))
			{
				Integer motion = inputState.getMotion(input);
				if (motion != null)
				{
					inputState.consumeMotion(input);
					if (motion > 0)
					{
						if (this.holds <= 0)
						{
							System.out.println("PRESS!");
							this.holds = 0;
							this.motion = 1;
						}
						++this.holds;
					}
					else if (motion < 0)
					{
						--this.holds;
						if (this.holds <= 0)
						{
							System.out.println("RELEASE!");
							this.holds = 0;
							this.motion = -1;
						}
					}
				}
			}
		}
	}

	@Override
	public boolean getState()
	{
		return this.holds > 0;
	}

	@Override
	public int getAction()
	{
		return this.motion;
	}
}