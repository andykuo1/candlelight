package org.qsilver.material;

import org.bstone.component.ComponentManager;

/**
 * Created by Andy on 6/8/17.
 */
public class MaterialManager extends ComponentManager<Material, Property>
{
	public MaterialManager()
	{
		super(Property.class);
	}

	public Material createMaterial(Property... properties)
	{
		Material mat = new Material();
		for (Property prop : properties)
		{
			this.addComponentToEntity(mat, prop);
		}
		return mat;
	}
}
