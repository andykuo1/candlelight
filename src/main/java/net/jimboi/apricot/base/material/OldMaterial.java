package net.jimboi.apricot.base.material;

import net.jimboi.apricot.base.component.ManifestEntity;

/**
 * Created by Andy on 6/8/17.
 */
public class OldMaterial extends ManifestEntity<OldProperty>
{
	OldMaterial(){}

	public boolean allowDefaultProperty(Class<? extends OldProperty> property)
	{
		return true;
	}
}
