package apricot.base.render.material;

import apricot.bstone.util.ColorUtil;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 8/10/17.
 */
public class PropertyColor extends Property<ContextColor>
{
	public static final PropertyColor PROPERTY = new PropertyColor(new ContextColor());

	public static final String COLOR_NAME = "color";

	public static final Vector3fc COLOR_DEFAULT = new Vector3f(1, 1, 1);

	protected PropertyColor(ContextColor context)
	{
		super(context);
	}

	@Override
	protected void addSupportForMaterial(Material material)
	{
		material.addProperty(COLOR_NAME, Vector3fc.class, COLOR_DEFAULT);
	}

	public Vector3fc getNormalizedRGB(Material material)
	{
		return material.getProperty(Vector3fc.class, COLOR_NAME);
	}

	public int getColor(Material material)
	{
		Vector3fc color = this.getNormalizedRGB(material);
		if (color == null) return 0;
		return ColorUtil.getColor(color);
	}
}
