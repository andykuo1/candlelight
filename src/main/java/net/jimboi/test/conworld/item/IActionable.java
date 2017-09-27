package net.jimboi.test.conworld.item;

import net.jimboi.test.conworld.action.Action;

import java.util.List;

/**
 * Created by Andy on 8/30/17.
 */
public interface IActionable
{
	List<Action> getAvailableActions(ItemStack itemstack, List<Action> dst);
}
