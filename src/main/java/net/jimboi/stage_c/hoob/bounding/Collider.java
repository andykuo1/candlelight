package net.jimboi.stage_c.hoob.bounding;

import org.bstone.transform.Transform2;
import org.joml.Vector2fc;

import java.util.Iterator;

/**
 * Created by Andy on 7/14/17.
 */
public class Collider
{
	protected final BoundingManager boundingManager;
	protected final BoundingShape shape;

	Collider(BoundingManager boundingManager, BoundingShape shape)
	{
		this.boundingManager = boundingManager;
		this.shape = shape;
	}

	public void update(float x, float y)
	{
		this.shape.center.set(x, y);
	}

	public Vector2fc checkIntersection()
	{
		Iterator<Shape> iter = this.boundingManager.getShapeIterator();
		while (iter.hasNext())
		{
			Shape other = iter.next();
			if (other == this.shape) continue;

			IntersectionData intersection = this.shape.intersects(other);
			if (intersection != null)
			{
				return intersection.delta;
			}
		}

		return Transform2.ZERO;
	}

	public Vector2fc checkCollision(Vector2fc delta)
	{
		IntersectionData data = Collision.collides((ShapeAABB) this.shape, this.boundingManager.getShapeIterator(), delta);
		return data == null ? delta : data.delta;
	}

	public BoundingShape getShape()
	{
		return this.shape;
	}
}
