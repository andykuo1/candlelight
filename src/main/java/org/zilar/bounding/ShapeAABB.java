package org.zilar.bounding;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/14/17.
 */
public class ShapeAABB extends Shape
{
	protected final Vector2f radius;

	public ShapeAABB(float x, float y, float radius)
	{
		super(x, y);

		this.radius = new Vector2f(radius, radius);
	}

	public ShapeAABB(float x, float y, float width, float height)
	{
		super(x, y);

		this.radius = new Vector2f(width / 2, height / 2);
	}

	public Vector2fc radius()
	{
		return this.radius;
	}
}
