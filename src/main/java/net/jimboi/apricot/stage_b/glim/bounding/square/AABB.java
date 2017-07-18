package net.jimboi.apricot.stage_b.glim.bounding.square;

import org.joml.Vector2f;

/**
 * Created by Andy on 6/3/17.
 */
public class AABB extends Bounding2D
{
	public final Vector2f radius = new Vector2f();

	public AABB(float centerX, float centerY, float radiusX, float radiusY)
	{
		super(centerX, centerY);
		this.radius.set(radiusX, radiusY);
	}
}
