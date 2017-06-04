package net.jimboi.glim.bounding.square;

import net.jimboi.glim.bounding.Intersection;
import net.jimboi.glim.bounding.IntersectionData;

import org.bstone.util.MathUtil;
import org.joml.Vector3f;

/**
 * Created by Andy on 6/3/17.
 */
public class Intersection2D
{
	public static void initialize()
	{
		Intersection.register(AABB.class, AABB.class, Intersection2D::testStatic);

		Intersection.register(AABB.class, Point.class, Intersection2D::testStatic);

		Intersection.register(Point.class, AABB.class, (a, b) -> Intersection2D.testStatic(b, a));

		Intersection.register(AABB.class, Circle.class, (a, b) ->
		{
			float dx = b.center.x - a.center.x;
			float px = (b.radius + a.radius.x) - Math.abs(dx);
			if (px <= 0) return null;

			float dy = b.center.y - a.center.y;
			float py = (b.radius + a.radius.y) - Math.abs(dy);
			if (py <= 0) return null;

			Vector3f pos = new Vector3f();
			Vector3f normal = new Vector3f();
			Vector3f delta = new Vector3f();

			if (px < py)
			{
				float sx = MathUtil.sign(dx);
				delta.x = px * sx;
				normal.x = sx;
				pos.x = a.center.x + (a.radius.x * sx);
				pos.z = b.center.y;
			}
			else
			{
				float sy = MathUtil.sign(dy);
				delta.z = py * sy;
				normal.z = sy;
				pos.x = b.center.x;
				pos.z = a.center.y + (a.radius.y * sy);
			}

			return new IntersectionData(a, pos, normal, delta);
		});

		Intersection.register(Circle.class, Circle.class, (a, b) ->
		{
			float r = a.radius + b.radius;

			float dx = b.center.x - a.center.x;
			float px = r - Math.abs(dx);
			if (px <= 0) return null;

			float dy = b.center.y - a.center.y;
			float py = r - Math.abs(dy);
			if (py <= 0) return null;

			Vector3f pos = new Vector3f();
			Vector3f normal = new Vector3f();
			Vector3f delta = new Vector3f();

			float sx = MathUtil.sign(dx);
			float sy = MathUtil.sign(dy);
			delta.x = px * sx;
			delta.z = py * sy;
			normal.x = sx;
			normal.z = sy;
			pos.x = a.center.x + (r * sx);
			pos.z = a.center.y + (r * sy);

			return new IntersectionData(a, pos, normal, delta);
		});

		Intersection.register(Circle.class, Point.class, (a, b) ->
		{
			float distsqu = b.center.distanceSquared(a.center);

			if (distsqu > a.radius * a.radius) return null;

			Vector3f pos = new Vector3f();
			Vector3f normal = new Vector3f();
			Vector3f delta = new Vector3f();

			delta.x = b.center.x;
			delta.z = b.center.y;

			delta.x -= a.center.x;
			delta.z -= a.center.y;

			pos.x = a.center.x + a.radius;
			pos.z = a.center.y + a.radius;

			normal.set(delta).normalize();

			return new IntersectionData(a, pos, normal, delta);
		});

		Intersection.register(Point.class, Point.class, (a, b) ->
		{
			if (!a.center.equals(b.center)) return null;

			return new IntersectionData(a, new Vector3f(a.center.x, 0, a.center.y), new Vector3f(), new Vector3f());
		});
	}

	public static IntersectionData testStatic(AABB a, AABB b)
	{
		float dx = b.center.x - a.center.x;
		float px = (b.radius.x + a.radius.x) - Math.abs(dx);
		if (px <= 0) return null;

		float dy = b.center.y - a.center.y;
		float py = (b.radius.y + a.radius.y) - Math.abs(dy);
		if (py <= 0) return null;

		Vector3f pos = new Vector3f();
		Vector3f normal = new Vector3f();
		Vector3f delta = new Vector3f();

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			delta.x = px * sx;
			normal.x = sx;
			pos.x = a.center.x + (a.radius.x * sx);
			pos.z = b.center.y;
		}
		else
		{
			float sy = MathUtil.sign(dy);
			delta.z = py * sy;
			normal.z = sy;
			pos.x = b.center.x;
			pos.z = a.center.y + (a.radius.y * sy);
		}

		return new IntersectionData(a, pos, normal, delta);
	}

	public static IntersectionData testStatic(AABB a, Point b)
	{
		float dx = b.center.x - a.center.x;
		float px = a.radius.x - Math.abs(dx);
		if (px <= 0) return null;

		float dy = b.center.y - a.center.y;
		float py = a.radius.y - Math.abs(dy);
		if (py <= 0) return null;

		Vector3f pos = new Vector3f();
		Vector3f normal = new Vector3f();
		Vector3f delta = new Vector3f();

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			delta.x = px * sx;
			normal.x = sx;
			pos.x = a.center.x + (a.radius.x * sx);
			pos.z = b.center.y;
		}
		else
		{
			float sy = MathUtil.sign(dy);
			delta.y = py * sy;
			normal.z = sy;
			pos.x = b.center.x;
			pos.z = a.center.y + (a.radius.y * sy);
		}

		return new IntersectionData(a, pos, normal, delta);
	}
}
