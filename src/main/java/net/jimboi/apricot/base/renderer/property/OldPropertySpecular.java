package net.jimboi.apricot.base.renderer.property;

import net.jimboi.apricot.base.material.OldProperty;

import org.joml.Vector3f;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 6/8/17.
 */
public class OldPropertySpecular extends OldProperty
{
	public final Vector3f specularColor = new Vector3f(1, 1, 1);
	public float shininess = 80F;

	public OldPropertySpecular setSpecularColor(int color)
	{
		ColorUtil.getNormalizedRGB(color, this.specularColor);
		return this;
	}
}
