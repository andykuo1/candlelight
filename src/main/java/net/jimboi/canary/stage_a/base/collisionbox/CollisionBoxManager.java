package net.jimboi.canary.stage_a.base.collisionbox;

import net.jimboi.canary.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.canary.stage_a.base.collisionbox.response.CollisionSolver;

import org.qsilver.util.iterator.FilterIterator;

import java.util.Iterator;

/**
 * Created by Andy on 8/7/17.
 */
public class CollisionBoxManager
{
	public void update(Iterable<BoxCollider> colliders)
	{
		for(BoxCollider collider : colliders)
		{
			if (collider.isColliderActive())
			{
				collider.onPreCollisionUpdate();
				CollisionSolver.checkCollision(collider, colliders, collider::canCollideWith, collider::onCollision);
				collider.onPostCollisionUpdate();
			}
		}
	}

	public Iterable<BoxCollider> getNearestColliders(Iterator<BoxCollider> colliders, float x, float y, float radius)
	{
		return () -> new FilterIterator<>(colliders,
				(collider) -> collider.getBoundingBox().isWithinRange(x, y, radius));
	}
}
