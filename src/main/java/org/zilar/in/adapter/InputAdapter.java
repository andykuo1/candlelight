package org.zilar.in.adapter;

import org.zilar.in.InputState;
import org.zilar.in.provider.InputProvider;

/**
 * Created by Andy on 1/22/18.
 */
public abstract class InputAdapter
{
	protected final InputProvider provider;

	public InputAdapter(InputProvider provider)
	{
		this.provider = provider;
	}

	public abstract Boolean getState(InputState inputState);

	public abstract Integer getAction(InputState inputState);

	public abstract Float getRange(InputState inputState);

	public InputProvider getProvider()
	{
		return this.provider;
	}
}
