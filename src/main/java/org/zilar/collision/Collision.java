package org.zilar.collision;

import org.bstone.util.Pair;
import org.bstone.util.TriFunction;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.util.MathUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 7/20/17.
 */
public class Collision
{
	private static final Map<Pair<Class, Class>, TriFunction<Shape, Shape, CollisionData, CollisionData>> FUNCTIONS = new HashMap<>();

	private static final Pair<Class, Class> _PAIR = new Pair<>(null, null);

	@SuppressWarnings("unchecked")
	protected static <A extends Shape, B extends Shape> void register(Class<A> collider, Class<B> other, final TriFunction<A, B, CollisionData, CollisionData> function)
	{
		register(false, collider, other, function);
	}

	@SuppressWarnings("unchecked")
	protected static <A extends Shape, B extends Shape> void register(boolean unique, Class<A> collider, Class<B> other, final TriFunction<A, B, CollisionData, CollisionData> function)
	{
		_PAIR.set(collider, other);
		if (FUNCTIONS.containsKey(_PAIR))
		{
			throw new IllegalArgumentException("Already registered collision function for pair '" + collider.getSimpleName() + "' and '" + other.getSimpleName() + "'.");
		}

		FUNCTIONS.put(new Pair<>(collider, other), (a, b, c) -> function.apply((A) a, (B) b, c));

		if (!unique && !collider.equals(other))
		{
			_PAIR.set(other, collider);
			if (FUNCTIONS.containsKey(_PAIR))
			{
				throw new IllegalArgumentException("Already registered unique collision function for pair '" + collider.getSimpleName() + "' and '" + other.getSimpleName() + "'.");
			}

			FUNCTIONS.put(new Pair<>(other, collider), (b, a, c) -> {
				CollisionData data = function.apply((A) a, (B) b, c);
				if (data != null)
				{
					data.deltaX = -data.deltaX;
					data.deltaY = -data.deltaY;
					//TODO: if you do this, the normal and point values are invalid!
					data.normalX = 0;data.normalY = 0;data.pointX = 0;data.pointY = 0;
				}
				return data;
			});
		}
	}

	@SuppressWarnings("unchecked")
	protected static <A extends Shape, B extends Shape> TriFunction<A, B, CollisionData, CollisionData> get(A collider, B other)
	{
		_PAIR.set(collider.getClass(), other.getClass());
		return (TriFunction<A, B, CollisionData, CollisionData>) FUNCTIONS.get(_PAIR);
	}

	static
	{
		register(true, Shape.Point.class, Shape.Point.class, Collision::test);
		register(Shape.Circle.class, Shape.Point.class, Collision::test);
		//register(Shape.Circle.class, Shape.Segment.class, Collision::test);
		register(true, Shape.Circle.class, Shape.AABB.class, Collision::test);
		register(true, Shape.Circle.class, Shape.Circle.class, Collision::test);
		register(Shape.AABB.class, Shape.Point.class, Collision::test);
		register(Shape.AABB.class, Shape.Segment.class, Collision::test);
		register(true, Shape.AABB.class, Shape.AABB.class, Collision::test);
		register(true, Shape.AABB.class, Shape.Circle.class, Collision::test);
		//register(Shape.Segment.class, Shape.Point.class, Collision::test);
		//register(Shape.Segment.class, Shape.Segment.class, Collision::test);
		//register(Shape.Segment.class, Shape.AABB.class, Collision::test);
		//register(Shape.Segment.class, Shape.Circle.class, Collision::test);
	}

	public static <A extends Shape, B extends Shape> CollisionData doCollisionTest(A collider, B other, CollisionData dst)
	{
		TriFunction<A, B, CollisionData, CollisionData> function = get(collider, other);
		if (function == null) throw new UnsupportedOperationException("Collision between '" + collider.getClass().getSimpleName() + "' and '" + other.getClass().getSimpleName() + "' is not yet implemented!");
		return function.apply(collider, other, dst);
	}

	private static final Vector2f _VEC2_A = new Vector2f();
	private static final Vector2f _VEC2_B = new Vector2f();

	private static CollisionData test(Shape.Point collider, Shape.Point other, CollisionData dst)
	{
		if (collider.getCenter(_VEC2_A).equals(other.getCenter(_VEC2_B)))
		{
			float sign = MathUtil.sign(collider.hashCode() - other.hashCode());
			dst.deltaX = Float.MIN_NORMAL * sign;
			dst.deltaY = Float.MIN_NORMAL * sign;

			dst.pointX = _VEC2_A.x();
			dst.pointY = _VEC2_A.y();

			dst.normalX = 0;
			dst.normalY = 0;

			return dst;
		}

		return null;
	}

	private static CollisionData test(Shape.Circle collider, Shape.Point other, CollisionData dst)
	{
		final float radius = collider.getRadius();

		if (collider.getCenter(_VEC2_A).distanceSquared(other.getCenter(_VEC2_B)) < radius * radius)
		{
			float dist = _VEC2_A.distance(_VEC2_B);
			float deltaMult = radius - dist;

			Vector2fc deltaNorm = _VEC2_B.sub(_VEC2_A).normalize();
			dst.normalX = deltaNorm.x();
			dst.normalY = deltaNorm.y();

			Vector2fc delta = deltaNorm.mul(deltaMult, _VEC2_A);
			dst.deltaX = delta.x();
			dst.deltaY = delta.y();

			Vector2fc point = deltaNorm.mul(radius, _VEC2_B).add(collider.getCenter(_VEC2_A));
			dst.pointX = point.x();
			dst.pointY = point.y();

			return dst;
		}

		return null;
	}

	private static CollisionData test(Shape.Circle collider, Shape.AABB other, CollisionData dst)
	{
		if (test(other, collider, dst) != null)
		{
			dst.deltaX = -dst.deltaX;
			dst.deltaY = -dst.deltaY;

			Vector2fc normal = other.getCenter(_VEC2_B).sub(collider.getCenter(_VEC2_A), _VEC2_B).normalize();
			dst.normalX = normal.x();
			dst.normalY = normal.y();

			Vector2fc point = normal.mul(collider.getRadius(), _VEC2_B).add(collider.getCenter(_VEC2_A), _VEC2_B);
			dst.pointX = point.x();
			dst.pointY = point.y();

			return dst;
		}
		return null;
	}

	private static CollisionData test(Shape.Circle collider, Shape.Circle other, CollisionData dst)
	{
		final float radius = collider.getRadius();
		final float otherRadius = other.getRadius();

		if (collider.getCenter(_VEC2_A).distanceSquared(other.getCenter(_VEC2_B)) < radius * radius + otherRadius * otherRadius)
		{
			float dist = _VEC2_A.distance(_VEC2_B);
			float deltaMult = otherRadius - (dist - radius);
			Vector2fc deltaNorm = _VEC2_B.sub(_VEC2_A, _VEC2_B).normalize();
			dst.normalX = deltaNorm.x();
			dst.normalY = deltaNorm.y();

			Vector2fc delta = deltaNorm.mul(deltaMult, _VEC2_A);
			dst.deltaX = delta.x();
			dst.deltaY = delta.y();

			Vector2fc point = deltaNorm.mul(radius, _VEC2_B).add(collider.getCenter(_VEC2_A));
			dst.pointX = point.x();
			dst.pointY = point.y();

			return dst;
		}

		return null;
	}

	private static CollisionData test(Shape.AABB collider, Shape.Point other, CollisionData dst)
	{
		return test(collider, other.getCenter(_VEC2_A), dst);
	}

	private static CollisionData test(Shape.AABB collider, Vector2fc point, CollisionData dst)
	{
		float dx = point.x() - collider.getCenterX();
		float px = collider.getHalfWidth() - Math.abs(dx);
		if (px <= 0) return null;

		float dy = point.y() - collider.getCenterY();
		float py = collider.getHalfHeight() - Math.abs(dy);
		if (py <= 0) return null;

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			dst.deltaX = px * sx;
			dst.normalX = sx;
			dst.pointX = collider.getCenterX() + (collider.getHalfWidth() * sx);
			dst.pointY = point.y();

			dst.deltaY = 0;
			dst.normalY = 0;
		}
		else
		{
			float sy = MathUtil.sign(dy);
			dst.deltaY = py * sy;
			dst.normalY = sy;
			dst.pointX = point.x();
			dst.pointY = collider.getCenterY() + (collider.getHalfHeight() * sy);

			dst.deltaX = 0;
			dst.normalX = 0;
		}

		return dst;
	}

	private static CollisionData test(Shape.AABB collider, Shape.Segment other, CollisionData dst)
	{
		Vector2fc point = other.getCenter(_VEC2_A);
		Vector2fc delta = other.getDelta(_VEC2_B);
		float paddingX = 0;
		float paddingY = 0;

		float scaleX = 1.0F / delta.x();
		float scaleY = 1.0F / delta.y();
		float signX = MathUtil.sign(scaleX);
		float signY = MathUtil.sign(scaleY);

		float deltaTimeX = signX * (collider.getHalfWidth() + paddingX);
		float deltaTimeY = signY * (collider.getHalfHeight() + paddingY);
		float nearTimeX = (collider.getHalfWidth() - deltaTimeX - point.x()) * scaleX;
		float nearTimeY = (collider.getHalfHeight() - deltaTimeY - point.y()) * scaleY;
		float farTimeX = (collider.getHalfWidth() + deltaTimeX - point.x()) * scaleX;
		float farTimeY = (collider.getHalfHeight() + deltaTimeY - point.y()) * scaleY;

		if (nearTimeX > farTimeY || nearTimeY > farTimeY) return null;

		float nearTime = Math.max(nearTimeX, nearTimeY);
		float farTime = Math.min(farTimeX, farTimeY);

		if (nearTime >= 1 || farTime <= 0) return null;

		float dt = MathUtil.clamp(nearTime, 0, 1);
		if (nearTimeX > nearTimeY)
		{
			dst.normalX = -signX;
			dst.normalY = 0;
		}
		else
		{
			dst.normalX = 0;
			dst.normalY = -signY;
		}

		dst.deltaX = dt * delta.x();
		dst.deltaY = dt * delta.y();
		dst.pointX = point.x() + dst.deltaX;
		dst.pointY = point.y() + dst.deltaY;
		return dst;
	}

	private static CollisionData test(Shape.AABB collider, Shape.AABB other, CollisionData dst)
	{
		float dx = other.getCenterX() - collider.getCenterX();
		float px = (other.getHalfWidth() + collider.getHalfWidth()) - Math.abs(dx);
		if (px <= 0) return null;

		float dy = other.getCenterY() - collider.getCenterY();
		float py = (other.getHalfHeight() + collider.getHalfHeight()) - Math.abs(dy);
		if (py <= 0) return null;

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			dst.deltaX = px * sx;
			dst.normalX = sx;
			dst.pointX = collider.getCenterX() + (collider.getHalfWidth() * sx);
			dst.pointY = other.getCenterY();

			dst.deltaY = 0;
			dst.normalY = 0;
		}
		else
		{
			float sy = MathUtil.sign(dy);
			dst.deltaY = py * sy;
			dst.normalY = sy;
			dst.pointX = other.getCenterX();
			dst.pointY = collider.getCenterY() + (collider.getHalfHeight() * sy);

			dst.deltaX = 0;
			dst.normalX = 0;
		}

		return dst;
	}

	private static CollisionData test(Shape.AABB collider, Shape.Circle other, CollisionData dst)
	{
		Vector2fc deltaNorm = collider.getCenter(_VEC2_A).sub(other.getCenter(_VEC2_B), _VEC2_A).normalize();
		Vector2fc point = deltaNorm.mul(other.getRadius(), _VEC2_B).add(other.getCenter(_VEC2_A), _VEC2_A);
		return test(collider, point, dst);
	}

	//SWEEP TESTS!

	private static CollisionData testSweep(Shape.AABB other, Vector2fc point, Vector2fc delta, CollisionData dst)
	{
		float paddingX = other.getHalfWidth();
		float paddingY = other.getHalfHeight();

		float scaleX = 1.0F / delta.x();
		float scaleY = 1.0F / delta.y();
		float signX = MathUtil.sign(scaleX);
		float signY = MathUtil.sign(scaleY);

		float deltaTimeX = signX * (other.getHalfWidth() + paddingX);
		float deltaTimeY = signY * (other.getHalfHeight() + paddingY);
		float nearTimeX = (other.getHalfWidth() - deltaTimeX - point.x()) * scaleX;
		float nearTimeY = (other.getHalfHeight() - deltaTimeY - point.y()) * scaleY;
		float farTimeX = (other.getHalfWidth() + deltaTimeX - point.x()) * scaleX;
		float farTimeY = (other.getHalfHeight() + deltaTimeY - point.y()) * scaleY;

		if (nearTimeX > farTimeY || nearTimeY > farTimeY) return null;

		float nearTime = Math.max(nearTimeX, nearTimeY);
		float farTime = Math.min(farTimeX, farTimeY);

		if (nearTime >= 1 || farTime <= 0) return null;

		float dt = MathUtil.clamp(nearTime, 0, 1);
		if (nearTimeX > nearTimeY)
		{
			dst.normalX = -signX;
			dst.normalY = 0;
		}
		else
		{
			dst.normalX = 0;
			dst.normalY = -signY;
		}

		dst.deltaX = dt * delta.x();
		dst.deltaY = dt * delta.y();
		dst.pointX = point.x() + dst.deltaX;
		dst.pointY = point.y() + dst.deltaY;
		return dst;
	}
}
