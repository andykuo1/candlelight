package net.jimboi.stage_c.gui;

import java.util.function.Consumer;

/**
 * Created by Andy on 7/12/17.
 */
public class GuiButton extends Gui
{
	private final Consumer<GuiButton> actionHandler;

	public GuiButton(int x, int y, int width, int height, Consumer<GuiButton> actionHandler)
	{
		super(x, y, width, height);

		this.actionHandler = actionHandler;
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
		this.actionHandler.accept(this);
	}
}
