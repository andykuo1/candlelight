package net.jimboi.test.conworld.view;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleUtil;
import net.jimboi.test.conworld.acor.Actor;
import net.jimboi.test.conworld.item.ItemStack;

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
		ConsoleUtil.title(console, this.actor.getName());

		ConsoleUtil.button(console, "Refresh", this::refresh);

		for(ItemStack itemstack : this.actor.getItems())
		{
			ConsoleUtil.message(console, itemstack.getStackSize() + "x " + itemstack.getItem().getName());
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
