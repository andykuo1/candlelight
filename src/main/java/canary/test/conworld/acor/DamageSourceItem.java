package canary.test.conworld.acor;

import canary.test.conworld.item.ItemStack;

/**
 * Created by Andy on 8/30/17.
 */
public class DamageSourceItem extends DamageSource
{
	private ItemStack itemstack;

	public DamageSourceItem(Actor owner, ItemStack itemstack)
	{
		super(owner);

		this.itemstack = itemstack;
	}

	public ItemStack getUsedItem()
	{
		return this.itemstack;
	}
}
