package apricot.base.material;

import apricot.base.component.ManifestEntity;

/**
 * Created by Andy on 6/8/17.
 */
@Deprecated
public class OldMaterial extends ManifestEntity<OldProperty>
{
	OldMaterial(){}

	public boolean allowDefaultProperty(Class<? extends OldProperty> property)
	{
		return true;
	}
}
