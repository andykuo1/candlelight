package net.jimboi.apricot.base.gui;

import net.jimboi.apricot.base.gui.base.Gui;
import net.jimboi.apricot.base.gui.base.GuiSelector;

/**
 * Created by Andy on 7/16/17.
 */
public class GuiFrame extends Gui
{
	private boolean dragged = false;
	private float offsetX;
	private float offsetY;

	@Override
	protected void onUpdate()
	{
		super.onUpdate();

		if (this.dragged)
		{
			GuiSelector selector = this.getGuiManager().getSelector();
			this.setPosition(selector.getX() - this.offsetX, selector.getY() - this.offsetY);
		}
	}

	@Override
	protected void onGuiStateChanged(int state)
	{
		super.onGuiStateChanged(state);

		if (state == 2 && this.prevState == 1)
		{
			GuiSelector selector = this.getGuiManager().getSelector();
			selector.grabFocus(this);
			this.dragged = true;
			this.offsetX = this.getGuiManager().getSelector().getX() - this.getX();
			this.offsetY = this.getGuiManager().getSelector().getY() - this.getY();
		}
		else if (state == 3)
		{
			GuiSelector selector = this.getGuiManager().getSelector();
			selector.grabFocus(null);
			this.dragged = false;
			this.offsetX = 0;
			this.offsetY = 0;
		}
	}
}
