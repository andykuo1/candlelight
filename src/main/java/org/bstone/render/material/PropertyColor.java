package org.bstone.render.material;

import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/10/17.
 */
public class PropertyColor
{
	public static final String COLOR = "color";

	public static Material addProperty(Material material)
	{
		material.addProperty(COLOR, Vector4fc.class, new Vector4f(1, 1, 1, 0));
		return material;
	}

	public static Material setNormalizedRGBA(Material material, Vector4fc nrgba)
	{
		if (!material.hasProperty(COLOR))
		{
			material.addProperty(COLOR, Vector4fc.class);
		}

		material.setProperty(COLOR, nrgba);
		return material;
	}

	public static Material setColor(Material material, int color)
	{
		if (!material.hasProperty(COLOR))
		{
			material.addProperty(COLOR, Vector4fc.class);
		}

		material.setProperty(COLOR, ColorUtil.getNormalizedRGBA(color, new Vector4f()));
		return material;
	}

	public static Vector4fc getNormalizedRGBA(Material material)
	{
		return material.getProperty(Vector4fc.class, COLOR);
	}

	public static int getColor(Material material)
	{
		Vector4fc nrgba = material.getProperty(Vector4fc.class, COLOR);
		if (nrgba == null) return 0;
		return ColorUtil.getColor(nrgba);
	}
}
