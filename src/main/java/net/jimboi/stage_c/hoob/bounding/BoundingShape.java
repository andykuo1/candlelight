package net.jimboi.stage_c.hoob.bounding;

import org.joml.Vector2fc;

/**
 * Created by Andy on 7/14/17.
 */
public abstract class BoundingShape extends Shape
{
	public BoundingShape(float x, float y)
	{
		super(x, y);
	}

	public abstract IntersectionData intersects(Shape other);

	public abstract IntersectionData collides(Shape other, Vector2fc delta);
}
