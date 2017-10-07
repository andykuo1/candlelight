package net.jimboi.test.conworld.action;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleUtil;
import net.jimboi.test.conworld.acor.Actor;
import net.jimboi.test.conworld.acor.DamageSourceItem;
import net.jimboi.test.conworld.item.ItemStack;
import net.jimboi.test.conworld.item.ItemSword;
import net.jimboi.test.conworld.view.ActorSelectorView;
import net.jimboi.test.conworld.world.World;

/**
 * Created by Andy on 8/30/17.
 */
public class ActionAttack extends Action
{
	private final ItemStack itemstack;

	public ActionAttack(ItemStack itemstack)
	{
		this.itemstack = itemstack;
	}

	@Override
	public void execute(World world, Actor owner)
	{
		new ActorSelectorView(world,
				(actor) -> owner != actor,
				(actor) -> {
					final Console console = world.getCurrentConsole();
					final ItemSword item = (ItemSword) this.itemstack.getItem();

					ConsoleUtil.message(console,  "Attacked " + actor.getName() + " with a " + item.getName() + "!");
					int damage = item.getDamage();
					actor.applyDamage(new DamageSourceItem(owner, this.itemstack), damage);

					ConsoleUtil.message(console, "Dealt " + damage + " damage!");

					owner.onUseAction(this);
				}).create();
	}

	@Override
	public int getActionPoints()
	{
		return 1;
	}

	@Override
	public String getName()
	{
		return this.itemstack.getItem().getName() + "_attack";
	}
}
