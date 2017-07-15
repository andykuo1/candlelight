package net.jimboi.stage_c.hoob.bounding;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/14/17.
 */
public class ShapeSegment extends Shape
{
	protected Vector2f delta;

	public ShapeSegment(float x, float y, float dx, float dy)
	{
		super(x, y);

		this.delta = new Vector2f(dx, dy);
	}

	public Vector2fc getDelta()
	{
		return this.delta;
	}
}
