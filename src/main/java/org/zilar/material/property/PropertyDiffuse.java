package org.zilar.material.property;

import org.bstone.material.Property;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 6/8/17.
 */
public class PropertyDiffuse extends Property
{
	public final Vector4f diffuseColor = new Vector4f(1, 1, 1, 0);

	public PropertyDiffuse setDiffuseColor(int color)
	{
		Vector3f vec = ColorUtil.getNormalizedRGB(color, new Vector3f());
		this.diffuseColor.x = vec.x;
		this.diffuseColor.y = vec.y;
		this.diffuseColor.z = vec.z;
		this.diffuseColor.w = 1;
		return this;
	}
}
