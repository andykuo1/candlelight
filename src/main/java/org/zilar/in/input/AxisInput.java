package org.zilar.in.input;

import org.zilar.in.provider.InputProvider;
import org.zilar.in.state.RawInputState;

/**
 * Created by Andy on 1/23/18.
 */
public final class AxisInput
{
	private final InputProvider provider;
	private final int id;

	public AxisInput(InputProvider provider, int id)
	{
		this.provider = provider;
		this.id = id;
	}

	public void offset(RawInputState src, float offset)
	{
		src.offsetAxis(this.id, offset);
	}

	public void set(RawInputState src, float value)
	{
		src.setAxis(this.id, value);
	}

	public Float get(RawInputState src)
	{
		return src.getAxis(this.id);
	}

	public Float getMotion(RawInputState src)
	{
		return src.getAxisMotion(this.id);
	}

	public InputProvider getProvider()
	{
		return this.provider;
	}

	public int getID()
	{
		return this.id;
	}
}
