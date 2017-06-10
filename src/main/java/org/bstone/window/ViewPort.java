package org.bstone.window;

/**
 * Created by Andy on 6/7/17.
 */
public class ViewPort
{
	private int width;
	private int height;

	public ViewPort(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}
}
