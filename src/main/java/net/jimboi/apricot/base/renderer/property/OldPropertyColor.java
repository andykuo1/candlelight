package net.jimboi.apricot.base.renderer.property;

import net.jimboi.apricot.base.material.OldProperty;

import org.bstone.util.ColorUtil;
import org.joml.Vector4f;
import org.joml.Vector4fc;

/**
 * Created by Andy on 8/5/17.
 */
public class OldPropertyColor extends OldProperty
{
	private final Vector4f color = new Vector4f();

	public OldPropertyColor(int color)
	{
		ColorUtil.getNormalizedRGBA(color, this.color);
		this.color.w = 1;
	}

	public OldPropertyColor setColor(int color)
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
