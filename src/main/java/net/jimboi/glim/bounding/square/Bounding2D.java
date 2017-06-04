package net.jimboi.glim.bounding.square;

import net.jimboi.glim.bounding.Bounding;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 6/4/17.
 */
public abstract class Bounding2D implements Bounding
{
	static
	{
		Intersection2D.initialize();
	}

	public final Vector2f center = new Vector2f();

	public Bounding2D(float centerX, float centerY)
	{
		this.center.set(centerX, centerY);
	}

	@Override
	public void update(float x, float y, float z)
	{
		this.center.x = x;
		this.center.y = z;
	}

	@Override
	public void offset(float x, float y, float z)
	{
		this.center.x += x;
		this.center.y += z;
	}

	@Override
	public Vector3fc position()
	{
		return new Vector3f(this.center.x, 0, this.center.y);
	}
}
