package net.jimboi.apricot.stage_c.hoob.collision;

import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 7/20/17.
 */
public class CollisionManager
{
	private Set<Shape> shapes = new HashSet<>();

	public Collider createCollider(Shape shape)
	{
		this.shapes.add(shape);
		return new Collider(this, shape);
	}

	public void destroyCollider(Collider collider)
	{
		this.shapes.remove(collider.getShape());
		collider.collisionManager = null;
	}

	public StaticCollider createStatic(Shape shape)
	{
		this.shapes.add(shape);
		return new StaticCollider(this, shape);
	}

	public void destroyStatic(StaticCollider collider)
	{
		this.shapes.remove(collider.getShape());
		collider.collisionManager = null;
	}

	public CollisionData checkCollision(Shape collider, Vector2f velocity)
	{
		collider.setCenter(collider.getCenterX() + velocity.x(), collider.getCenterY() + velocity.y());
		CollisionData data = this.checkCollision(collider);
		collider.setCenter(collider.getCenterX() - velocity.x(), collider.getCenterY() - velocity.y());
		if (data != null)
		{
			data.deltaX += velocity.x();
			data.deltaY += velocity.y();
		}
		return data;
	}

	public CollisionData checkCollision(Shape collider)
	{
		Iterator<Shape> iter = this.shapes.iterator();
		CollisionData data = new CollisionData();
		float length = -1;

		CollisionData temp = new CollisionData();
		Vector2f delta = new Vector2f();

		while(iter.hasNext())
		{
			Shape other = iter.next();
			if (other == collider) continue;

			if (Collision.checkCollision(collider, other, temp) != null)
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

	public Iterator<Shape> getShapes()
	{
		return this.shapes.iterator();
	}
}
