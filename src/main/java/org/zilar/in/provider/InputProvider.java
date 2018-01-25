package org.zilar.in.provider;

import org.zilar.in.input.AxisInput;
import org.zilar.in.input.ButtonInput;
import org.zilar.in.state.RawInputState;

/**
 * Created by Andy on 1/22/18.
 */
public interface InputProvider
{
	default AxisInput getAxis(int id)
	{
		return new AxisInput(this, id);
	}

	default AxisInput[] getAxis(int... ids)
	{
		AxisInput[] arr = new AxisInput[ids.length];
		for(int i = 0; i < ids.length; ++i)
		{
			arr[i] = this.getAxis(ids[i]);
		}
		return arr;
	}

	default ButtonInput getButton(int id)
	{
		return new ButtonInput(this, id);
	}

	default ButtonInput[] getButton(int... ids)
	{
		ButtonInput[] arr = new ButtonInput[ids.length];
		for(int i = 0; i < ids.length; ++i)
		{
			arr[i] = this.getButton(ids[i]);
		}
		return arr;
	}

	RawInputState getProviderState();
}
