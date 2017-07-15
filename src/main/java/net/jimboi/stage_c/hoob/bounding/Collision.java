package net.jimboi.stage_c.hoob.bounding;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.util.MathUtil;

import java.util.Iterator;

/**
 * Created by Andy on 7/14/17.
 */
public class Collision
{
	public static IntersectionData testCollision(BoundingShape shape, Shape other, Vector2fc delta)
	{
		return shape.collides(other, delta);
	}

	public static IntersectionData collides(ShapeAABB shape, ShapeAABB other, Vector2fc delta)
	{
		CollisionData sweep = new CollisionData();

		if (delta.x() == 0 && delta.y() == 0)
		{
			sweep.point.x = other.center.x;
			sweep.point.y = other.center.y;
			sweep.intersection = Intersection.intersects(shape, other);
			if (sweep.intersection != null)
			{
				sweep.time = 0;
			}
			else
			{
				sweep.time = 1;
			}
		}
		else
		{
			sweep.intersection = Intersection.intersects(shape, other.center, delta, other.radius.x, other.radius.y);
			if (sweep.intersection != null)
			{
				sweep.time = MathUtil.clamp(sweep.time - Float.MIN_NORMAL, 0, 1);
				sweep.point.x = other.center.x + delta.x() * sweep.time;
				sweep.point.y = other.center.y + delta.y() * sweep.time;
				Vector2f dir = new Vector2f(delta);
				dir.normalize();
				sweep.intersection.point.x += dir.x * other.radius.x;
				sweep.intersection.point.y += dir.y * other.radius.y;
			}
			else
			{
				sweep.point.x = other.center.x + delta.x();
				sweep.point.y = other.center.y + delta.y();
				sweep.time = 1;
			}
		}

		return sweep.intersection;
	}

	public static IntersectionData collides(ShapeAABB shape, Iterator<Shape> others, Vector2fc delta)
	{
		IntersectionData nearest = new IntersectionData();
		nearest.point.set(shape.center).add(delta);
		nearest.delta.set(delta);
		float nearTime = delta.lengthSquared();
		while(others.hasNext())
		{
			Shape other = others.next();
			if (other instanceof ShapeAABB)
			{
				ShapeAABB otherAABB = (ShapeAABB) other;
				if (otherAABB == shape) continue;

				IntersectionData sweep = collides(otherAABB, shape, delta);
				if (sweep == null) continue;

				float time = sweep.delta.lengthSquared();
				if (time < nearTime)
				{
					nearest = sweep;
					nearTime = time;
				}
			}
		}

		return nearest;
	}
}
