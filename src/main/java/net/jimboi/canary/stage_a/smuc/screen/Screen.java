package net.jimboi.canary.stage_a.smuc.screen;

import net.jimboi.canary.stage_a.smuc.RasterizedView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 11/21/17.
 */
public class Screen
{
	private final List<ViewComponent> components = new ArrayList<>();

	public void render(RasterizedView view)
	{
		for(ViewComponent component : this.components)
		{
			component.render(view);
		}
	}
}
