package net.jimboi.canary.stage_a.smuc.screen;

import net.jimboi.canary.stage_a.smuc.RasterizedView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 11/21/17.
 */
public class ViewComponent
{
	protected int x, y;
	protected List<ViewComponent> components = new ArrayList<>();

	public ViewComponent(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void render(RasterizedView view)
	{
		for(ViewComponent component : this.components)
		{
			component.render(view);
		}
	}
}
