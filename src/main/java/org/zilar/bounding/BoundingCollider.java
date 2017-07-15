package org.zilar.bounding;

import org.joml.Vector2f;

/**
 * Created by Andy on 7/14/17.
 */
public class BoundingCollider extends Bounding
{
	BoundingCollider(BoundingManager boundingManager, Shape shape)
	{
		super(boundingManager, shape);
	}

	public void update(float x, float y)
	{
		this.shape.center.set(x, y);
	}

	public IntersectionData checkCollision(float dx, float dy)
	{
		return Collision.checkCollision(this.shape, this.boundingManager.getShapeIterator(), dx, dy);
	}

	public static Vector2f checkDeltaWithSlideCollision(BoundingCollider collider, Vector2f delta)
	{
		IntersectionData intersection = collider.checkCollision(delta.x(), delta.y());
		if (intersection != null)
		{
			Vector2f normal = intersection.normal.mul(intersection.delta.dot(intersection.normal), delta);
			intersection.delta.sub(normal, delta);
		}
		return delta;
	}
}
