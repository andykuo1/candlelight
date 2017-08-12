package net.jimboi.apricot.base.renderer.property;

import net.jimboi.apricot.base.material.OldProperty;

/**
 * Created by Andy on 6/8/17.
 */
public class OldPropertyShadow extends OldProperty
{
	public boolean castShadow;
	public boolean receiveShadow;

	public OldPropertyShadow(boolean cast, boolean receive)
	{
		this.castShadow = cast;
		this.receiveShadow = receive;
	}
}
