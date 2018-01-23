package org.zilar.in.adapter;

import org.zilar.in.InputProvider;
import org.zilar.in.InputState;

/**
 * Created by Andy on 1/22/18.
 */
public abstract class InputAdapter
{
	protected final String name;
	protected final InputProvider provider;

	public InputAdapter(String name, InputProvider provider)
	{
		this.name = name;
		this.provider = provider;
	}

	public abstract Boolean getState(InputState inputState);

	public abstract Integer getAction(InputState inputState);

	public abstract Float getRange(InputState inputState);

	public boolean isValid(InputState inputState)
	{
		return true;
	}

	public InputProvider getProvider()
	{
		return this.provider;
	}

	public String getName()
	{
		return this.name;
	}
}
