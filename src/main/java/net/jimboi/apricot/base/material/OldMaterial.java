package net.jimboi.apricot.base.material;

import org.bstone.component.ManifestEntity;

/**
 * Created by Andy on 6/8/17.
 */
public class OldMaterial extends ManifestEntity<Property>
{
	OldMaterial(){}

	public boolean allowDefaultProperty(Class<? extends Property> property)
	{
		return true;
	}
}
