package net.jimboi.test.conworld.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 8/30/17.
 */
public abstract class Item
{
	private static final Map<String, Item> ITEMS = new HashMap<>();

	public static Item register(String name, Item item)
	{
		if (ITEMS.containsKey(name))
		{
			throw new IllegalArgumentException("Found item with duplicate name '" + name + "' already registered!");
		}

		if (ITEMS.containsValue(item))
		{
			throw new IllegalArgumentException("Found duplicate item with name '" + name + "' already registered!");
		}

		item.name = name;
		ITEMS.put(name, item);
		return item;
	}

	public static Item getItem(String name)
	{
		return ITEMS.get(name);
	}

	public static Iterable<Item> getItems()
	{
		return ITEMS.values();
	}

	private String name;

	public int getMaxStackSize()
	{
		return 1;
	}

	public final String getName()
	{
		return this.name;
	}
}
