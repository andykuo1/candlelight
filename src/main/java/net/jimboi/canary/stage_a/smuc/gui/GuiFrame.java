package net.jimboi.canary.stage_a.smuc.gui;

/**
 * Created by Andy on 11/28/17.
 */
public final class GuiFrame extends GuiBase
{
	GuiFrame()
	{
		super(null);
	}

	public void updateScreenSize(float width, float height)
	{
		if (this.screenW != width || this.screenH != height)
		{
			this.screenW = width;
			this.screenH = height;

			this.update();
		}
	}
}
