package org.bstone.util;

/**
 * Created by Andy on 8/2/17.
 */
public class Counter
{
	protected int count;

	public int next()
	{
		return ++this.count;
	}

	public int peek()
	{
		return this.count;
	}

	public int poll()
	{
		int i = this.count;
		this.count = 0;
		return i;
	}

	public void reset()
	{
		this.count = 0;
	}
}
