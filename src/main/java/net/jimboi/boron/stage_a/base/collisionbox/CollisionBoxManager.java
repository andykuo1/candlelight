package net.jimboi.boron.stage_a.base.collisionbox;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionSolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 8/7/17.
 */
public class CollisionBoxManager
{
	private final Set<BoxCollider> colliders = new HashSet<>();
	private final Set<ActiveBoxCollider> actives = new HashSet<>();

	public void clear()
	{
		this.colliders.clear();
		this.actives.clear();
	}

	public void add(BoxCollider collider)
	{
		this.colliders.add(collider);
		if (collider instanceof ActiveBoxCollider)
		{
			this.actives.add((ActiveBoxCollider) collider);
		}
	}

	public void remove(BoxCollider collider)
	{
		this.colliders.remove(collider);
		if (collider instanceof ActiveBoxCollider)
		{
			this.actives.remove(collider);
		}
	}

	public void update()
	{
		for(ActiveBoxCollider collider : this.actives)
		{
			collider.onPreCollisionUpdate();
			CollisionSolver.checkCollision(collider, this.colliders, collider::canCollideWith, collider::onCollision);
			collider.onPostCollisionUpdate();
		}
	}

	public Iterable<BoxCollider> getColliders()
	{
		return this.colliders;
	}

	public Iterable<ActiveBoxCollider> getActiveColliders()
	{
		return this.actives;
	}
}
