package net.jimboi.stage_b.gnome.material.property;

import org.bstone.material.Property;

/**
 * Created by Andy on 6/8/17.
 */
public class PropertyShadow extends Property
{
	public boolean castShadow;
	public boolean receiveShadow;

	public PropertyShadow(boolean cast, boolean receive)
	{
		this.castShadow = cast;
		this.receiveShadow = receive;
	}
}
