package net.jimboi.test.conworld.view;

import net.jimboi.test.conworld.acor.Actor;
import net.jimboi.test.conworld.item.ItemStack;

import org.bstone.console.Console;
import org.bstone.console.ConsoleStyle;

/**
 * Created by Andy on 8/30/17.
 */
public class InventoryView extends View
{
	protected final Actor actor;

	public InventoryView(Actor actor)
	{
		super(new Console(320, 480));

		this.actor = actor;
	}

	public void refresh()
	{
		this.getConsole().clear();
		this.initialize(this.getConsole());
	}

	@Override
	protected void initialize(Console console)
	{
		ConsoleStyle.title(console, this.actor.getName());

		ConsoleStyle.button(console, "Refresh", this::refresh);

		for(ItemStack itemstack : this.actor.getItems())
		{
			ConsoleStyle.message(console, itemstack.getStackSize() + "x " + itemstack.getItem().getName());
		}
	}

	@Override
	protected void terminate(Console console)
	{
	}

	public final Actor getActor()
	{
		return this.actor;
	}
}
