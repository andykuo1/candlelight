package net.jimboi.boron.stage_a.gordo;

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

	public abstract void render(RasterView view);
}
