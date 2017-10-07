package net.jimboi.test.sleuth.comm.item;

import net.jimboi.test.sleuth.comm.goap.Attribute;

/**
 * Created by Andy on 9/30/17.
 */
public class ItemMaterial
{
	private final Attribute[] attributes;

	public ItemMaterial(Attribute... attributes)
	{
		this.attributes = attributes;
	}

	public final Attribute[] getAttributes()
	{
		return this.attributes;
	}
}
