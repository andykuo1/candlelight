package org.zilar.collision;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Andy on 7/22/17.
 */
public class CollisionSolver
{
	protected CollisionManager collisionManager;

	public CollisionSolver(CollisionManager collisionManager)
	{
		this.collisionManager = collisionManager;
	}

	public CollisionData checkCollision(Shape collider, Vector2f velocity)
	{
		collider.setCenter(collider.getCenterX() + velocity.x(), collider.getCenterY() + velocity.y());
		CollisionData data = this.checkNearestCollision(collider);
		collider.setCenter(collider.getCenterX() - velocity.x(), collider.getCenterY() - velocity.y());
		if (data != null)
		{
			data.deltaX += velocity.x();
			data.deltaY += velocity.y();
		}
		return data;
	}

	public CollisionData[] checkCollisions(Shape collider)
	{
		Iterator<Shape> iter = this.collisionManager.getShapes();
		ArrayList<CollisionData> collisions = new ArrayList<>();

		CollisionData data = new CollisionData();

		while(iter.hasNext())
		{
			Shape other = iter.next();
			if (other == collider) continue;

			if (Collision.doCollisionTest(collider, other, data) != null)
			{
				collisions.add(data);
				data = new CollisionData();
			}
		}

		return collisions.isEmpty() ? null : collisions.toArray(new CollisionData[collisions.size()]);
	}

	public CollisionData checkNearestCollision(Shape collider)
	{
		Iterator<Shape> iter = this.collisionManager.getShapes();
		CollisionData data = new CollisionData();
		float length = -1;

		CollisionData temp = new CollisionData();
		Vector2f delta = new Vector2f();

		while(iter.hasNext())
		{
			Shape other = iter.next();
			if (other == collider) continue;

			if (Collision.doCollisionTest(collider, other, temp) != null)
			{
				float f = delta.set(temp.deltaX, temp.deltaY).lengthSquared();
				if (length == -1 || f < length)
				{
					CollisionData old = data;
					data = temp;
					temp = old;
					length = f;
				}
			}
		}

		return length == -1 ? null : data;
	}
}
