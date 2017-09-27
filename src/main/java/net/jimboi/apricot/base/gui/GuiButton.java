package net.jimboi.apricot.base.gui;

import net.jimboi.apricot.base.gui.base.Gui;

import java.util.function.Consumer;

/**
 * Created by Andy on 7/16/17.
 */
public class GuiButton extends Gui
{
	private final Consumer<GuiButton> callback;

	public GuiButton(Consumer<GuiButton> callback)
	{
		this.callback = callback;
	}

	@Override
	protected void onGuiStateChanged(int state)
	{
		super.onGuiStateChanged(state);

		if (state == 3)
		{
			this.onGuiActivate();
		}
	}

	protected void onGuiActivate()
	{
		this.callback.accept(this);
	}
}
