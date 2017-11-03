package org.zilar.render.material;

import org.bstone.util.ColorUtil;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 8/12/17.
 */
public class ContextColor extends Context
{
	public ContextColor setNormalizedRGB(Vector3fc nrgb)
	{
		this.material.setProperty(PropertyColor.COLOR_NAME, nrgb);
		return this;
	}

	public ContextColor setColor(int color)
	{
		this.material.setProperty(PropertyColor.COLOR_NAME, ColorUtil.getNormalizedRGB(color, new Vector3f()));
		return this;
	}
}
