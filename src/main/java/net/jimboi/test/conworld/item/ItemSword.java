package net.jimboi.test.conworld.item;

import net.jimboi.test.conworld.action.Action;
import net.jimboi.test.conworld.action.ActionAttack;

import java.util.List;

/**
 * Created by Andy on 8/30/17.
 */
public class ItemSword extends Item implements IActionable
{
	@Override
	public List<Action> getAvailableActions(ItemStack itemstack, List<Action> dst)
	{
		dst.add(new ActionAttack(itemstack));
		return dst;
	}

	public int getDamage()
	{
		return 2;
	}
}
