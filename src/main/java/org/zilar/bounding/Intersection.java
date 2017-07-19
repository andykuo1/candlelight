package org.zilar.bounding;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 7/14/17.
 */
public class Intersection
{
	public static IntersectionData checkIntersection(Shape shape, Shape other)
	{
		if (shape instanceof ShapeAABB)
		{
			ShapeAABB aabb = (ShapeAABB) shape;
			if (other instanceof ShapeAABB)
			{
				return intersects(aabb, (ShapeAABB) other);
			}
			else if (other instanceof ShapeCircle)
			{
				return intersects(aabb, (ShapeCircle) other);
			}
			else if (other instanceof ShapeSegment)
			{
				return intersects(aabb, (ShapeSegment) other);
			}
			else if (other instanceof ShapePoint)
			{
				return intersects(aabb, (ShapePoint) other);
			}
		}
		else if (shape instanceof ShapeCircle)
		{
			ShapeCircle circle = (ShapeCircle) shape;
			if (other instanceof ShapePoint)
			{
				return intersects(circle, (ShapePoint) other);
			}
		}

		throw new UnsupportedOperationException("Intersection between shapes '" + shape.getClass() + "' and '" + other.getClass() + "' are not supported!");
	}

	protected static IntersectionData intersects(ShapeAABB shape, Vector2fc other)
	{
		float dx = other.x() - shape.center().x();
		float px = shape.radius().x() - Math.abs(dx);
		if (px <= 0) return null;

		float dy = other.y() - shape.center().y();
		float py = shape.radius().y() - Math.abs(dy);
		if (py <= 0) return null;

		IntersectionData hit = new IntersectionData();

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			hit.delta.x = px * sx;
			hit.normal.x = sx;
			hit.point.x = shape.center().x() + (shape.radius().x() * sx);
			hit.point.y = other.y();
		}
		else
		{
			float sy = MathUtil.sign(dy);
			hit.delta.y = py * sy;
			hit.normal.y = sy;
			hit.point.x = other.x();
			hit.point.y = shape.center().y() + (shape.radius().y() * sy);
		}

		return hit;
	}

	protected static IntersectionData intersects(ShapeAABB shape, ShapePoint other)
	{
		return intersects(shape, other.center());
	}

	protected static IntersectionData intersects(ShapeAABB shape, Vector2fc point, Vector2fc delta, float paddingX, float paddingY)
	{
		float scaleX = 1.0F / delta.x();
		float scaleY = 1.0F / delta.y();
		float signX = MathUtil.sign(scaleX);
		float signY = MathUtil.sign(scaleY);

		float deltaTimeX = signX * (shape.radius().x() + paddingX);
		float deltaTimeY = signY * (shape.radius().y() + paddingY);
		float nearTimeX = (shape.center().x() - deltaTimeX - point.x()) * scaleX;
		float nearTimeY = (shape.center().y() - deltaTimeY - point.y()) * scaleY;
		float farTimeX = (shape.center().x() + deltaTimeX - point.x()) * scaleX;
		float farTimeY = (shape.center().y() + deltaTimeY - point.y()) * scaleY;

		if (nearTimeX > farTimeY || nearTimeY > farTimeY) return null;

		float nearTime = Math.max(nearTimeX, nearTimeY);
		float farTime = Math.min(farTimeX, farTimeY);

		if (nearTime >= 1 || farTime <= 0) return null;

		IntersectionData intersection = new IntersectionData();
		float dt = MathUtil.clamp(nearTime, 0, 1);
		if (nearTimeX > nearTimeY)
		{
			intersection.normal.x = -signX;
			intersection.normal.y = 0;
		}
		else
		{
			intersection.normal.x = 0;
			intersection.normal.y = -signY;
		}

		intersection.delta.x = dt * delta.x();
		intersection.delta.y = dt * delta.y();
		intersection.point.x = point.x() + intersection.delta.x;
		intersection.point.y = point.y() + intersection.delta.y;
		return intersection;
	}

	protected static IntersectionData intersects(ShapeAABB shape, ShapeSegment other)
	{
		return intersects(shape, other.center(), other.delta(), 0, 0);
	}

	protected static IntersectionData intersects(ShapeAABB shape, ShapeAABB other)
	{
		float dx = other.center().x() - shape.center().x();
		float px = (other.radius().x() + shape.radius().x()) - Math.abs(dx);
		if (px <= 0) return null;

		float dy = other.center().y() - shape.center().y();
		float py = (other.radius().y() + shape.radius().y()) - Math.abs(dy);
		if (py <= 0) return null;

		IntersectionData intersection = new IntersectionData();

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			intersection.delta.x = px * sx;
			intersection.normal.x = sx;
			intersection.point.x = shape.center().x() + (shape.radius().x() * sx);
			intersection.point.y = other.center().y();
		}
		else
		{
			float sy = MathUtil.sign(dy);
			intersection.delta.y = py * sy;
			intersection.normal.y = sy;
			intersection.point.x = other.center().x();
			intersection.point.y = shape.center().y() + (shape.radius().y() * sy);
		}

		return intersection;
	}

	protected static IntersectionData intersects(ShapeAABB shape, ShapeCircle other)
	{
		Vector2f vec = shape.center().sub(other.center(), new Vector2f()).normalize().mul(other.radius()).add(other.center());
		return intersects(shape, vec);
	}

	protected static IntersectionData intersects(ShapeCircle shape, Vector2fc other)
	{
		if (shape.center().distanceSquared(other) > shape.radius() * shape.radius()) return null;

		IntersectionData hit = new IntersectionData();

		other.sub(shape.center(), hit.normal).normalize();
		float dist = shape.center().distance(other);
		hit.normal.mul(dist, hit.point).add(shape.center());
		hit.delta.set(hit.normal).mul(shape.radius() - dist);

		return hit;
	}

	protected static IntersectionData intersects(ShapeCircle shape, ShapePoint other)
	{
		return intersects(shape, other.center());
	}
}
