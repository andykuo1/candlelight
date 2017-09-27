package net.jimboi.test.conworld.acor;

import net.jimboi.test.conworld.action.Action;
import net.jimboi.test.conworld.item.IActionable;
import net.jimboi.test.conworld.item.Item;
import net.jimboi.test.conworld.item.ItemStack;
import net.jimboi.test.conworld.world.Items;
import net.jimboi.test.conworld.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Andy on 8/29/17.
 */
public class Actor
{
	public final Stats stats = new Stats();
	public final Set<ItemStack> items = new HashSet<>();

	public final String name;

	public Actor(String name)
	{
		this.name = name;

		this.items.add(new ItemStack(Items.sword, 1));
	}

	public void onTurnStart(World world)
	{
		this.stats.actionPoints = this.stats.maxActionPoints;
	}

	public void onTurnStop(World world)
	{

	}

	public void onUseAction(Action action)
	{
		this.stats.actionPoints -= action.getActionPoints();
	}

	public boolean canUseAction(Action action)
	{
		return this.stats.actionPoints >= action.getActionPoints();
	}

	public boolean applyDamage(DamageSource damageSource, int damage)
	{
		this.stats.damageTaken += damage;
		return true;
	}

	public List<Action> getActions(List<Action> dst)
	{
		for(ItemStack itemstack : this.items)
		{
			final Item item = itemstack.getItem();

			if (item instanceof IActionable)
			{
				((IActionable) item).getAvailableActions(itemstack, dst);
			}
		}

		return dst;
	}

	public Iterable<ItemStack> getItems()
	{
		return this.items;
	}

	public Stats getStats()
	{
		return this.stats;
	}

	public String getName()
	{
		return this.name;
	}
}
