package org.zilar.in.provider;

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

	public InputProvider getProvider()
	{
		return this.provider;
	}

	public int getID()
	{
		return this.id;
	}
}
