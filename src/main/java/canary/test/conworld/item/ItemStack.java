package canary.test.conworld.item;

import canary.qsilver.util.MathUtil;

/**
 * Created by Andy on 8/30/17.
 */
public class ItemStack
{
	private final Item item;

	protected int stackSize;

	public ItemStack(Item item, int stackSize)
	{
		this.item = item;
		this.stackSize = stackSize;
	}

	public ItemStack setStackSize(int stackSize)
	{
		this.stackSize = stackSize;
		return this;
	}

	public ItemStack addStack(ItemStack stack)
	{
		int emptySize = this.item.getMaxStackSize() - this.stackSize;
		if (stack.stackSize <= emptySize)
		{
			this.stackSize += stack.stackSize;
			stack.stackSize = 0;
			return null;
		}
		else
		{
			int diff = stack.stackSize - emptySize;
			this.stackSize = this.item.getMaxStackSize();
			stack.stackSize -= diff;
			return stack;
		}
	}

	public ItemStack removeStack(int amount)
	{
		amount = MathUtil.clamp(amount, 0, this.stackSize);
		this.stackSize -= amount;
		return new ItemStack(this.item, amount);
	}

	public int getStackSize()
	{
		return this.stackSize;
	}

	public boolean isEmpty()
	{
		return this.stackSize <= 0;
	}

	public final Item getItem()
	{
		return this.item;
	}
}
