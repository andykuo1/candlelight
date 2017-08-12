package net.jimboi.boron.stage_a.base.collisionbox.response;

import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.box.BoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.box.GridAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.box.GridBasedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;

import org.qsilver.util.MathUtil;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Andy on 8/7/17.
 */
public class CollisionSolver
{
	public static void checkCollision(ActiveBoxCollider collider, Iterable<BoxCollider> colliders, Predicate<BoxCollider> canCollideWith, Predicate<CollisionResponse> onCollision)
	{
		AxisAlignedBoundingBox boundingBox = collider.getBoundingBox();
		checkCollision(boundingBox, colliders, canCollideWith, onCollision);
	}

	public static void checkCollision(AxisAlignedBoundingBox boundingBox, Iterable<BoxCollider> colliders, Predicate<BoxCollider> canCollideWith, Predicate<CollisionResponse> onCollision)
	{
		for(BoxCollider other : colliders)
		{
			if (!other.isSolid()) continue;
			if (!canCollideWith.test(other)) continue;

			BoundingBox otherBox = other.getBoundingBox();
			if (otherBox == boundingBox) continue;
			if (otherBox == null) continue;

			if (otherBox instanceof AxisAlignedBoundingBox)
			{
				//AABB & GABB SOLUTIONS
				AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox) otherBox;
				CollisionResponse response = solve(boundingBox, aabb);
				if (response != null)
				{
					response.collider = other;
					if (onCollision.test(response))
					{
						return;
					}
				}
			}
			else if (otherBox instanceof GridBasedBoundingBox)
			{
				//GBBB SOLUTIONS
				GridBasedBoundingBox gbbb = (GridBasedBoundingBox) otherBox;
				List<GridAlignedBoundingBox> others = gbbb.getSubBox(boundingBox.getCenterX(), boundingBox.getCenterY(), boundingBox.getHalfWidth(), boundingBox.getHalfHeight());
				if (!others.isEmpty())
				{
					for(GridAlignedBoundingBox gabb : others)
					{
						CollisionResponse response = solve(boundingBox, gabb);
						if (response != null)
						{
							response.collider = other;
							if (onCollision.test(response))
							{
								return;
							}
						}
					}
				}
			}
			else
			{
				throw new IllegalArgumentException("Collision between '" + boundingBox.getClass().getSimpleName() + "' and '" + otherBox.getClass().getSimpleName() + "' not yet supported!");
			}
		}
	}

	public static CollisionResponse solve(AxisAlignedBoundingBox collider, AxisAlignedBoundingBox other)
	{
		float dx = collider.getCenterX() - other.getCenterX();
		float maxDistX = collider.getHalfWidth() + other.getHalfWidth();
		float overlapX = maxDistX - Math.abs(dx);
		if (overlapX <= 0) return null;

		float dy = collider.getCenterY() - other.getCenterY();
		float maxDistY = collider.getHalfHeight() + other.getHalfHeight();
		float overlapY = maxDistY - Math.abs(dy);
		if (overlapY <= 0) return null;

		CollisionResponse response = new CollisionResponse();
		if (overlapX < overlapY)
		{
			float signX = MathUtil.sign(dx);
			response.delta.set(overlapX * signX, 0);
			response.normal.set(signX, 0);
			response.point.set(other.getCenterX() + (other.getHalfWidth() * signX), collider.getCenterY());
		}
		else
		{
			float signY = MathUtil.sign(dy);
			response.delta.set(0, overlapY * signY);
			response.normal.set(0, signY);
			response.point.set(collider.getCenterX(), other.getCenterY() + (other.getHalfHeight() * signY));
		}
		return response;
	}

	public static CollisionResponse solve(float posX, float posY, float dx, float dy, float paddingX, float paddingY, AxisAlignedBoundingBox other)
	{
		float scaleX = 1F / dx;
		float scaleY = 1F / dy;
		float signX = MathUtil.sign(scaleX);
		float signY = MathUtil.sign(scaleY);
		float segmentWidth = signX * (other.getHalfWidth() + paddingX);
		float segmentHeight = signY * (other.getHalfHeight() + paddingY);
		float nearTimeX = (other.getCenterX() - segmentWidth - posX) * scaleX;
		float nearTimeY = (other.getCenterY() - segmentHeight - posY) * scaleY;
		float farTimeX = (other.getCenterX() + segmentWidth - posX) * scaleX;
		float farTimeY = (other.getCenterY() + segmentHeight - posY) * scaleY;

		if (nearTimeX > farTimeY || nearTimeY > farTimeX) return null;

		float nearTime = nearTimeX > nearTimeY ? nearTimeX : nearTimeY;
		float farTime = farTimeX < farTimeY ? farTimeX : farTimeY;

		if (nearTime >= 1 || farTime <= 0) return null;

		CollisionResponse response = new CollisionResponse();

		float hitTime = MathUtil.clamp(nearTime, 0, 1);
		if (nearTimeX > nearTimeY)
		{
			response.normal.set(-signX, 0);
		}
		else
		{
			response.normal.set(0, -signY);
		}

		response.delta.set(hitTime * dx);
		response.delta.set(hitTime * dy);
		response.point.set(posX + response.delta.x());
		response.point.set(posY + response.delta.y());
		return response;
	}

	public static CollisionResponse solve(AxisAlignedBoundingBox collider, GridAlignedBoundingBox other)
	{
		return solve(collider, (AxisAlignedBoundingBox) other);
	}

	public static CollisionResponse solve(float posX, float posY, float dx, float dy, float paddingX, float paddingY, GridAlignedBoundingBox other)
	{
		return solve(posX, posY, dx, dy, paddingX, paddingY, (AxisAlignedBoundingBox) other);
	}

	public static CollisionResponse solve(float posX, float posY, float dx, float dy, float paddingX, float paddingY, GridBasedBoundingBox other)
	{
		List<GridAlignedBoundingBox> others = other.getSubBox(posX, posY, dx, dy);
		if (!others.isEmpty())
		{
			CollisionResponse result = null;

			for(GridAlignedBoundingBox gabb : others)
			{
				CollisionResponse response = solve(posX, posY, -dx, -dy, paddingX, paddingY, gabb);
				if (response != null)
				{
					return response;
					/*
					if (result == null || result.getPoint().distanceSquared(posX, posY) > response.getPoint().distanceSquared(posX, posY))
					{
						result = response;
					}
					*/
				}
			}

			return result;
		}

		return null;
	}
}
