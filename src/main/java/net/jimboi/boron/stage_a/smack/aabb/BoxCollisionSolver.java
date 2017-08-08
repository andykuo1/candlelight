package net.jimboi.boron.stage_a.smack.aabb;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.util.MathUtil;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Andy on 8/7/17.
 */
public class BoxCollisionSolver
{
	public static BoxCollisionData checkFirstCollision(BoxCollider collider, Iterable<BoxCollider> colliders, Predicate<BoxCollider> canCollideWith)
	{
		for(BoxCollider other : colliders)
		{
			if (other == collider) continue;
			if (!other.isSolid()) continue;
			if (!canCollideWith.test(other)) continue;

			if (other instanceof GridCollider)
			{
				return null;
			}
			else
			{
				if (other.getBoundingBox() == null) continue;

				BoxCollisionData data = solve(collider.getBoundingBox(), other.getBoundingBox());
				if (data != null)
				{
					data.collider = other;
					return data;
				}
			}
		}

		return null;
	}

	public static void checkCollision(BoxCollider collider, Iterable<BoxCollider> colliders, Consumer<BoxCollisionData> onCollision, Predicate<BoxCollider> canCollideWith)
	{
		for(BoxCollider other : colliders)
		{
			if (other == collider) continue;
			if (!other.isSolid()) continue;
			if (!canCollideWith.test(other)) continue;

			if (other instanceof GridCollider)
			{
				GridCollisionSolver.checkCollision(collider, (GridCollider) other, onCollision);
			}
			else
			{
				if (other.getBoundingBox() == null) continue;

				BoxCollisionData data = solve(collider.getBoundingBox(), other.getBoundingBox());
				if (data != null)
				{
					data.collider = other;
					onCollision.accept(data);
				}
			}
		}
	}

	public static BoxCollisionData solve(AxisAlignedBoundingBox collider, AxisAlignedBoundingBox other)
	{
		float dx = collider.centerX - other.centerX;
		float maxDistX = collider.halfWidth + other.halfWidth;
		float overlapX = maxDistX - Math.abs(dx);
		if (overlapX <= 0) return null;

		float dy = collider.centerY - other.centerY;
		float maxDistY = collider.halfHeight + other.halfHeight;
		float overlapY = maxDistY - Math.abs(dy);
		if (overlapY <= 0) return null;

		BoxCollisionData data = new BoxCollisionData();
		if (overlapX < overlapY)
		{
			float signX = MathUtil.sign(dx);
			data.delta.set(overlapX * signX, 0);
			data.normal.set(signX, 0);
			data.point.set(other.centerX + (other.halfWidth * signX), collider.centerY);
		}
		else
		{
			float signY = MathUtil.sign(dy);
			data.delta.set(0, overlapY * signY);
			data.normal.set(0, signY);
			data.point.set(collider.centerX, other.centerY + (other.halfHeight * signY));
		}
		return data;
	}

	public static BoxCollisionData solve(AxisAlignedBoundingBox collider, AxisAlignedBoundingBox other, Vector2fc velocity, Vector2f dst)
	{
		if (velocity.x() == 0 && velocity.y() == 0)
		{
			BoxCollisionData data = solve(collider, other);
			if (data != null)
			{
				dst.set(0);
				return data;
			}
			else
			{
				dst.set(velocity);
				return null;
			}
		}
		else
		{
			BoxCollisionData data = solve(other, collider.centerX, collider.centerY, velocity.x(), velocity.y(), collider.halfWidth, collider.halfHeight);
			if (data != null)
			{
				dst.sub(data.delta);
				return data;
			}
			else
			{
				dst.set(velocity);
				return null;
			}
		}
	}

	private static BoxCollisionData solve(AxisAlignedBoundingBox collider, float posX, float posY, float dx, float dy, float paddingX, float paddingY)
	{
		float scaleX = 1F / dx;
		float scaleY = 1F / dy;
		float signX = MathUtil.sign(scaleX);
		float signY = MathUtil.sign(scaleY);
		float segmentWidth = signX * (collider.halfWidth + paddingX);
		float segmentHeight = signY * (collider.halfHeight + paddingY);
		float nearTimeX = (collider.centerX - segmentWidth - posX) * scaleX;
		float nearTimeY = (collider.centerY - segmentHeight - posY) * scaleY;
		float farTimeX = (collider.centerX + segmentWidth - posX) * scaleX;
		float farTimeY = (collider.centerY + segmentHeight - posY) * scaleY;

		if (nearTimeX > farTimeY || nearTimeY > farTimeX) return null;

		float nearTime = Math.max(nearTimeX, nearTimeY);
		float farTime = Math.min(farTimeX, farTimeY);

		if (nearTime >= 1 || farTime <= 0) return null;

		BoxCollisionData data = new BoxCollisionData();

		float hitTime = MathUtil.clamp(nearTime, 0, 1);
		if (nearTimeX > nearTimeY)
		{
			data.normal.set(-signX, 0);
		}
		else
		{
			data.normal.set(0, -signY);
		}

		data.delta.set(hitTime * dx);
		data.delta.set(hitTime * dy);
		data.point.set(posX + data.delta.x());
		data.point.set(posY + data.delta.y());
		return data;
	}
}
