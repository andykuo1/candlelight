package net.jimboi.mod2.material;

import net.jimboi.mod2.asset.Asset;
import net.jimboi.mod2.material.property.PropertyDiffuse;
import net.jimboi.mod2.material.property.PropertyShadow;
import net.jimboi.mod2.material.property.PropertySpecular;
import net.jimboi.mod2.material.property.PropertyTexture;

import org.bstone.mogli.Texture;
import org.qsilver.material.MaterialManager;
import org.qsilver.material.Property;

import java.util.ArrayList;

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

	public static Property[] getProperties(Asset<Texture> texture)
	{
		return getProperties(texture.getSource());
	}

	public static Property[] getProperties(Texture texture)
	{
		ArrayList<Property> properties = new ArrayList<>();
		properties.add(new PropertyDiffuse());
		properties.add(new PropertySpecular());
		properties.add(new PropertyShadow(true, true));
		if (texture != null)
		{
			properties.add(new PropertyTexture(texture));
		}
		return properties.toArray(new Property[properties.size()]);
	}

	public static Property[] getProperties(int color)
	{
		ArrayList<Property> properties = new ArrayList<>();
		PropertyDiffuse propDiffuse = new PropertyDiffuse();
		propDiffuse.setDiffuseColor(color);
		properties.add(propDiffuse);
		properties.add(new PropertySpecular());
		return properties.toArray(new Property[properties.size()]);
	}
}
