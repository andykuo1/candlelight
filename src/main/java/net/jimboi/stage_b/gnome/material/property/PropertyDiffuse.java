package net.jimboi.stage_b.gnome.material.property;

import org.bstone.material.Property;
import org.joml.Vector3f;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 6/8/17.
 */
public class PropertyDiffuse extends Property
{
	public final Vector3f diffuseColor = new Vector3f(1, 1, 1);

	public PropertyDiffuse setDiffuseColor(int color)
	{
		ColorUtil.getNormalizedRGB(color, this.diffuseColor);
		return this;
	}
}