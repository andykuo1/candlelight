package net.jimboi.canary.stage_a.owle;

import net.jimboi.canary.stage_a.smuc.RasterizedView;

/**
 * Created by Andy on 9/13/17.
 */
public abstract class ViewComponent
{
	public int x;
	public int y;

	public ViewComponent(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void update()
	{}

	public abstract void render(RasterizedView view);
}
