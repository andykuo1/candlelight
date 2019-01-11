package canary.test.conworld.item;

import canary.test.conworld.action.Action;

import java.util.List;

/**
 * Created by Andy on 8/30/17.
 */
public interface IActionable
{
	List<Action> getAvailableActions(ItemStack itemstack, List<Action> dst);
}
