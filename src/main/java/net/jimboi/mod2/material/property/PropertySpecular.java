package net.jimboi.mod2.material.property;

import org.bstone.util.ColorUtil;
import org.joml.Vector3f;
import org.qsilver.material.Property;

/**
 * Created by Andy on 6/8/17.
 */
public class PropertySpecular extends Property
{
	public final Vector3f specularColor = new Vector3f(1, 1, 1);
	public float shininess = 80F;

	public PropertySpecular setSpecularColor(int color)
	{
		ColorUtil.getNormalizedRGB(color, this.specularColor);
		return this;
	}
}
