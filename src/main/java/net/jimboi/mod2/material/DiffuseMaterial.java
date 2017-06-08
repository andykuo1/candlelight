package net.jimboi.mod2.material;

import net.jimboi.mod2.material.property.PropertyDiffuse;
import net.jimboi.mod2.material.property.PropertySpecular;
import net.jimboi.mod2.material.property.PropertyTexture;

import org.bstone.mogli.Texture;
import org.qsilver.material.Material;
import org.qsilver.material.MaterialManager;

/**
 * Created by Andy on 6/8/17.
 */
public class DiffuseMaterial
{
	private static MaterialManager MANAGER;

	public static void setMaterialManager(MaterialManager materialManager)
	{
		MANAGER = materialManager;
	}

	public static Material create(Texture texture)
	{
		Material mat = MANAGER.createMaterial(new PropertyDiffuse(), new PropertySpecular());
		if (texture != null) MANAGER.addComponentToEntity(mat, new PropertyTexture(texture));
		return mat;
	}
}
