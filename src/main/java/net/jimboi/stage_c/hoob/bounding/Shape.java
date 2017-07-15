package net.jimboi.stage_c.hoob.bounding;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/14/17.
 */
public abstract class Shape
{
	protected final Vector2f center;

	public Shape(float x, float y)
	{
		this.center = new Vector2f(x, y);
	}

	public Vector2fc center()
	{
		return this.center;
	}
}
