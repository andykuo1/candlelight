package org.zilar.in.input;

import org.zilar.in.provider.InputProvider;
import org.zilar.in.state.RawInputState;

/**
 * Created by Andy on 1/23/18.
 */
public final class ButtonInput
{
	private final InputProvider provider;
	private final int id;

	public ButtonInput(InputProvider provider, int id)
	{
		this.provider = provider;
		this.id = id;
	}

	public void press(RawInputState src)
	{
		src.pressButton(this.id);
	}

	public void release(RawInputState src)
	{
		src.releaseButton(this.id);
	}

	public void set(RawInputState src, int value)
	{
		src.setButton(this.id, value);
	}

	public Integer get(RawInputState src)
	{
		return src.getButton(this.id);
	}

	public Integer getMotion(RawInputState src)
	{
		return src.getButtonMotion(this.id);
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
