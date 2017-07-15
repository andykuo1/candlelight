package net.jimboi.stage_c.hoob.bounding;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/14/17.
 */
public class ShapeAABB extends BoundingShape
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

	public Vector2fc getRadius()
	{
		return this.radius;
	}

	@Override
	public IntersectionData intersects(Shape other)
	{
		if (other instanceof ShapeAABB)
		{
			return Intersection.intersects(this, (ShapeAABB) other);
		}
		else if (other instanceof ShapeCircle)
		{
			return Intersection.intersects(this, (ShapeCircle) other);
		}
		else if (other instanceof ShapePoint)
		{
			return Intersection.intersects(this, (ShapePoint) other);
		}
		else if (other instanceof ShapeSegment)
		{
			return Intersection.intersects(this, (ShapeSegment) other);
		}
		else
		{
			throw new UnsupportedOperationException("Intersection between shapes '" + this.getClass() + "' and '" + other.getClass() + "' are not supported!");
		}
	}

	@Override
	public IntersectionData collides(Shape other, Vector2fc delta)
	{
		if (other instanceof ShapeAABB)
		{
			return Collision.collides(this, (ShapeAABB) other, delta);
		}
		else
		{
			throw new UnsupportedOperationException("Intersection between shapes '" + this.getClass() + "' and '" + other.getClass() + "' are not supported!");
		}
	}
}
