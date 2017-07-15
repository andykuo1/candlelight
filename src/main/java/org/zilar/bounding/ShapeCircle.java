package org.zilar.bounding;

/**
 * Created by Andy on 7/14/17.
 */
public class ShapeCircle extends Shape
{
	protected float radius;

	public ShapeCircle(float x, float y, float radius)
	{
		super(x, y);

		this.radius = radius;
	}

	public float radius()
	{
		return this.radius;
	}
}
