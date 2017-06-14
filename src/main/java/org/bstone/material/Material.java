package org.bstone.material;

import org.bstone.component.EntityManifest;

/**
 * Created by Andy on 6/8/17.
 */
public class Material extends EntityManifest<Property>
{
	Material(){}

	public boolean allowDefaultProperty(Class<? extends Property> property)
	{
		return true;
	}
}
