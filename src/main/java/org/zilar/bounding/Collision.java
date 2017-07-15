package org.zilar.bounding;

import org.joml.Vector2f;

import java.util.Iterator;

/**
 * Created by Andy on 7/14/17.
 */
public class Collision
{
	public static IntersectionData checkCollision(Shape shape, Shape other, float dx, float dy)
	{
		if (shape instanceof ShapeAABB)
		{
			ShapeAABB aabb = (ShapeAABB) shape;
			if (other instanceof ShapeAABB)
			{
				return collides(aabb, (ShapeAABB) other, dx, dy);
			}
			else
			{
				shape.center.x += dx;
				shape.center.y += dy;
				IntersectionData intersection = Intersection.checkIntersection(shape, other);
				shape.center.x -= dx;
				shape.center.y -= dy;
				return intersection;
			}
		}
		else
		{
			if (other instanceof ShapeAABB)
			{
				other.center.x += dx;
				other.center.y += dy;
				IntersectionData intersection = Intersection.checkIntersection(other, shape);
				other.center.x -= dx;
				other.center.y -= dy;
				return intersection;
			}
		}

		throw new UnsupportedOperationException("Collision between shapes '" + shape.getClass() + "' and '" + other.getClass() + "' are not supported!");
	}

	public static IntersectionData checkCollision(Shape shape, Iterator<Shape> others, float dx, float dy)
	{
		IntersectionData nearest = new IntersectionData();
		nearest.point.set(shape.center()).add(dx, dy);
		nearest.delta.set(dx, dy);
		float nearTime = nearest.delta.lengthSquared();
		while(others.hasNext())
		{
			Shape other = others.next();
			if (other == shape) continue;

			IntersectionData sweep = checkCollision(other, shape, dx, dy);
			if (sweep == null) continue;

			float time = sweep.delta.lengthSquared();
			if (time < nearTime)
			{
				nearest = sweep;
				nearTime = time;
			}
		}

		return nearest;
	}

	protected static IntersectionData collides(ShapeAABB shape, ShapeAABB other, float dx, float dy)
	{
		IntersectionData intersection;

		if (dx == 0 && dy == 0)
		{
			intersection = Intersection.intersects(shape, other);
			return intersection;
		}
		else
		{
			Vector2f delta = new Vector2f(dx, dy);
			intersection = Intersection.intersects(shape, other.center(), delta, other.radius().x(), other.radius().y());
			if (intersection != null)
			{
				Vector2f dir = delta.normalize();
				intersection.point.x += dir.x * other.radius().x();
				intersection.point.y += dir.y * other.radius().y();
			}
			else
			{
				intersection = new IntersectionData();
				intersection.point.x = other.center().x() + delta.x();
				intersection.point.y = other.center().y() + delta.y();
				intersection.delta.set(delta);
			}
			return intersection;
		}
	}
}
