package org.bstone.material;

import org.bstone.component.ManifestEntity;

/**
 * Created by Andy on 6/8/17.
 */
public class Material extends ManifestEntity<Property>
{
	Material(){}

	public boolean allowDefaultProperty(Class<? extends Property> property)
	{
		return true;
	}
}
