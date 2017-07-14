package net.jimboi.stage_c.gui;

import net.jimboi.stage_a.mod.sprite.Sprite;

/**
 * Created by Andy on 7/12/17.
 */
public class GuiSprite extends Gui
{
	protected Sprite sprite;

	public GuiSprite(int x, int y, int width, int height, Sprite sprite)
	{
		super(x, y, width, height);

		this.sprite = sprite;
	}

	@Override
	protected void updateGui()
	{

	}

	@Override
	protected void onEnter()
	{

	}

	@Override
	protected void onLeave()
	{

	}

	@Override
	protected void onSelect()
	{

	}

	@Override
	protected void onAction()
	{

	}

	public void setSprite(Sprite sprite)
	{
		this.sprite = sprite;
	}

	public Sprite getSprite()
	{
		return this.sprite;
	}
}
