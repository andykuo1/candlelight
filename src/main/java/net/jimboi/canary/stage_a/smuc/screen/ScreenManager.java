package net.jimboi.canary.stage_a.smuc.screen;

import net.jimboi.canary.stage_a.smuc.RasterizedView;

import java.util.Stack;

/**
 * Created by Andy on 11/21/17.
 */
public class ScreenManager
{
	private final Stack<Screen> screens = new Stack<>();

	public void pushScreen(Screen screen)
	{
		this.screens.push(screen);
	}

	public Screen popScreen()
	{
		return this.screens.pop();
	}

	public Screen getCurrentScreen()
	{
		return this.screens.peek();
	}

	public void render(RasterizedView view)
	{
		if (!this.screens.isEmpty())
		{
			Screen screen = this.screens.peek();
			screen.render(view);
		}
	}
}
