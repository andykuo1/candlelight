package net.jimboi.stage_c.hoob.bounding;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 7/14/17.
 */
public class Intersection
{
	protected static IntersectionData intersects(ShapeAABB shape, ShapePoint other)
	{
		float dx = other.center.x - shape.center.x;
		float px = shape.radius.x - Math.abs(dx);
		if (px <= 0) return null;

		float dy = other.center.y - shape.center.y;
		float py = shape.radius.y - Math.abs(dy);
		if (py <= 0) return null;

		IntersectionData hit = new IntersectionData();

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			hit.delta.x = px * sx;
			hit.normal.x = sx;
			hit.point.x = shape.center.x + (shape.radius.x * sx);
			hit.point.y = other.center.y;
		}
		else
		{
			float sy = MathUtil.sign(dy);
			hit.delta.y = py * sy;
			hit.normal.y = sy;
			hit.point.x = other.center.x;
			hit.point.y = shape.center.y + (shape.radius.y * sy);
		}

		return hit;
	}

	protected static IntersectionData intersects(ShapeAABB shape, Vector2fc point, Vector2fc delta, float paddingX, float paddingY)
	{
		float scaleX = 1.0F / delta.x();
		float scaleY = 1.0F / delta.y();
		float signX = MathUtil.sign(scaleX);
		float signY = MathUtil.sign(scaleY);

		float deltaTimeX = signX * (shape.radius.x + paddingX);
		float deltaTimeY = signY * (shape.radius.y + paddingY);
		float nearTimeX = (shape.center.x - deltaTimeX - point.x()) * scaleX;
		float nearTimeY = (shape.center.y - deltaTimeY - point.y()) * scaleY;
		float farTimeX = (shape.center.x + deltaTimeX - point.x()) * scaleX;
		float farTimeY = (shape.center.y + deltaTimeY - point.y()) * scaleY;

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
		return intersects(shape, other.center, other.delta, 0, 0);
	}

	protected static IntersectionData intersects(ShapeAABB shape, ShapeAABB other)
	{
		float dx = other.center.x - shape.center.x;
		float px = (other.radius.x + shape.radius.x) - Math.abs(dx);
		if (px <= 0) return null;

		float dy = other.center.y - shape.center.y;
		float py = (other.radius.y + shape.radius.y) - Math.abs(dy);
		if (py <= 0) return null;

		IntersectionData hit = new IntersectionData();

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			hit.delta.x = px * sx;
			hit.normal.x = sx;
			hit.point.x = shape.center.x + (shape.radius.x * sx);
			hit.point.y = other.center.y;
		}
		else
		{
			float sy = MathUtil.sign(dy);
			hit.delta.y = py * sy;
			hit.normal.y = sy;
			hit.point.x = other.center.x;
			hit.point.y = shape.center.y + (shape.radius.y * sy);
		}

		return hit;
	}

	protected static IntersectionData intersects(ShapeAABB shape, ShapeCircle other)
	{
		Vector2f delta = new Vector2f(shape.center);
		delta.sub(other.center).normalize().mul(other.radius);
		return intersects(shape, other.center, delta, 0, 0);
	}
}
