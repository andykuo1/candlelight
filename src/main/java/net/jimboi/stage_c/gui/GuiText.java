package net.jimboi.stage_c.gui;

import net.jimboi.stage_c.gui.base.Gui;

/**
 * Created by Andy on 7/16/17.
 */
public class GuiText extends Gui
{
	private String text;

	public GuiText(String text)
	{
		this.text = text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return this.text;
	}

	@Override
	public boolean isSolid(float x, float y)
	{
		return false;
	}
}
