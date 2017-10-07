package net.jimboi.test.sleuth.comm.item;

import net.jimboi.test.sleuth.comm.goap.Attribute;
import net.jimboi.test.sleuth.comm.goap.AttributeSet;

/**
 * Created by Andy on 9/25/17.
 */
public class Item
{
	public final AttributeSet attributes = new AttributeSet();
	private final ItemMaterial material;
	public final int id;

	public Item(int id, ItemMaterial material)
	{
		this.id = id;
		this.material = material;

		for(Attribute attribute : this.material.getAttributes())
		{
			this.attributes.add(attribute);
		}
	}

	public ItemMaterial getMaterial()
	{
		return this.material;
	}

	public AttributeSet getAttributes()
	{
		return this.attributes;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Item)
		{
			return this.id == ((Item) o).id;
		}
		return false;
	}
}
