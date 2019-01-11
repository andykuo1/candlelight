package canary.test.conworld.world;

import canary.test.conworld.item.Item;
import canary.test.conworld.item.ItemBase;
import canary.test.conworld.item.ItemSword;

/**
 * Created by Andy on 8/30/17.
 */
public class Items
{
	public static final Item sword = Item.register("sword", new ItemSword());
	public static final Item flower = Item.register("flower", new ItemBase());
}
