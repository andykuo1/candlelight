package org.zilar.gui;

import org.zilar.gui.base.Gui;

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
