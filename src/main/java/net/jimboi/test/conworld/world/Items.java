package net.jimboi.test.conworld.world;

import net.jimboi.test.conworld.item.Item;
import net.jimboi.test.conworld.item.ItemBase;
import net.jimboi.test.conworld.item.ItemSword;

/**
 * Created by Andy on 8/30/17.
 */
public class Items
{
	public static final Item sword = Item.register("sword", new ItemSword());
	public static final Item flower = Item.register("flower", new ItemBase());
}
