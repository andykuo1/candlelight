package net.jimboi.stage_c.gui;

/**
 * Created by Andy on 7/12/17.
 */
public class GuiLabel extends Gui
{
	protected String text;

	public GuiLabel(int x, int y, int width, int height, String text)
	{
		super(x, y, width, height);

		this.text = text;
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

	public void setText(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return this.text;
	}
}
