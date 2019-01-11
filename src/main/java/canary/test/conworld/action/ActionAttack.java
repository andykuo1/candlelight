package canary.test.conworld.action;

import canary.test.conworld.acor.Actor;
import canary.test.conworld.acor.DamageSourceItem;
import canary.test.conworld.item.ItemStack;
import canary.test.conworld.item.ItemSword;
import canary.test.conworld.view.ActorSelectorView;
import canary.test.conworld.world.World;

import canary.zilar.console.Console;
import canary.zilar.console.ConsoleStyle;

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

					ConsoleStyle.message(console,  "Attacked " + actor.getName() + " with a " + item.getName() + "!");
					int damage = item.getDamage();
					actor.applyDamage(new DamageSourceItem(owner, this.itemstack), damage);

					ConsoleStyle.message(console, "Dealt " + damage + " damage!");

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
