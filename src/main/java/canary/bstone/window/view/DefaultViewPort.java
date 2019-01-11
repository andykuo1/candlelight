package canary.bstone.window.view;

import canary.bstone.window.Window;

/**
 * Created by Andy on 1/15/18.
 */
public final class DefaultViewPort extends ViewPort
{
	private final Window window;

	public DefaultViewPort(Window window)
	{
		super(0, 0, 0, 0);
		this.window = window;
	}

	@Override
	public void setWidth(int width)
	{
		throw new UnsupportedOperationException("Cannot change default viewport size!");
	}

	@Override
	public void setHeight(int height)
	{
		throw new UnsupportedOperationException("Cannot change default viewport size!");
	}

	@Override
	public int getX()
	{
		return 0;
	}

	@Override
	public int getY()
	{
		return 0;
	}

	@Override
	public int getWidth()
	{
		return this.window.getWidth();
	}

	@Override
	public int getHeight()
	{
		return this.window.getHeight();
	}
}
