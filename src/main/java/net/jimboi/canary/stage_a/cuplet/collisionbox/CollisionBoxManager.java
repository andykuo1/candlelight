package net.jimboi.canary.stage_a.cuplet.collisionbox;

import net.jimboi.canary.stage_a.cuplet.collisionbox.collider.BoxCollider;
import net.jimboi.canary.stage_a.cuplet.collisionbox.response.CollisionSolver;

import org.qsilver.util.iterator.FilterIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 8/7/17.
 */
public class CollisionBoxManager
{
	private final Set<BoxCollider> colliders = new HashSet<>();

	public void clear()
	{
		this.colliders.clear();
	}

	public void addCollider(BoxCollider collider)
	{
		this.colliders.add(collider);
	}

	public void removeCollider(BoxCollider collider)
	{
		this.colliders.remove(collider);
	}

	public void update()
	{
		for(BoxCollider collider : this.colliders)
		{
			if (collider.isColliderActive())
			{
				collider.onPreCollisionUpdate();
				CollisionSolver.checkCollision(collider, this.colliders, collider::canCollideWith, collider::onCollision);
				collider.onPostCollisionUpdate();
			}
		}
	}

	public Iterable<BoxCollider> getNearestColliders(float x, float y, float radius)
	{
		return new Iterable<BoxCollider>(){
			@Override
			public Iterator<BoxCollider> iterator()
			{
				return new FilterIterator<>(CollisionBoxManager.this.colliders.iterator(), ((collider) -> collider.getBoundingBox().isWithinRange(x, y, radius)));
			}
		};
	}

	public Iterable<BoxCollider> getColliders()
	{
		return this.colliders;
	}
}
