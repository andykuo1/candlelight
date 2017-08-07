package net.jimboi.apricot.base.renderer.property;

import org.bstone.material.Property;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/5/17.
 */
public class PropertyColor extends Property
{
	private final Vector4f color = new Vector4f();

	public PropertyColor(int color)
	{
		ColorUtil.getNormalizedRGBA(color, this.color);
		this.color.w = 1;
	}

	public PropertyColor setColor(int color)
	{
		ColorUtil.getNormalizedRGBA(color, this.color);
		this.color.w = 1;
		return this;
	}

	public Vector4fc getColor()
	{
		return this.color;
	}
}
