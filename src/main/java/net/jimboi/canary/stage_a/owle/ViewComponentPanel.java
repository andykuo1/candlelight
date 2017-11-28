package net.jimboi.canary.stage_a.owle;

import net.jimboi.canary.stage_a.smuc.RasterizedView;

/**
 * Created by Andy on 9/14/17.
 */
public class ViewComponentPanel extends ViewComponent
{
	public int width;
	public int height;

	protected int color = 0xFFFFFF;

	public ViewComponentPanel(int x, int y, int width, int height)
	{
		super(x, y);

		this.width = width;
		this.height = height;
	}

	@Override
	public void render(RasterizedView view)
	{
		if (this.width <= 0 || this.height <= 0) return;

		for(int i = 0; i < this.width; ++i)
		{
			for(int j = 0; j < this.height; ++j)
			{
				int k = 16;

				if (i != 0) k += 4;
				if (j != 0) k += 8;
				if (i != this.width - 1) k += 1;
				if (j != this.height - 1) k += 2;

				view.draw(this.x + i, this.y + j, (char) k, this.color);
			}
		}
	}

	public ViewComponentPanel setColor(int color)
	{
		this.color = color;
		return this;
	}

	public int getColor()
	{
		return this.color;
	}
}
