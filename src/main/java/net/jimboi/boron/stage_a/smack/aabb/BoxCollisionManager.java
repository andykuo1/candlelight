package net.jimboi.boron.stage_a.smack.aabb;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 8/7/17.
 */
public class BoxCollisionManager
{
	private final Set<BoxCollider> colliders = new HashSet<>();
	private final Set<ActiveBoxCollider> actives = new HashSet<>();
	private final Set<GridCollider> grids = new HashSet<>();

	public void add(BoxCollider collider)
	{
		if (collider instanceof GridCollider)
		{
			this.grids.add((GridCollider) collider);
		}
		else
		{
			this.colliders.add(collider);
			if (collider instanceof ActiveBoxCollider)
			{
				this.actives.add((ActiveBoxCollider) collider);
			}
		}
	}

	public void remove(BoxCollider collider)
	{
		if (collider instanceof GridCollider)
		{
			this.grids.remove(collider);
		}
		else
		{
			this.colliders.remove(collider);
			if (collider instanceof ActiveBoxCollider)
			{
				this.actives.remove(collider);
			}
		}
	}

	public void update()
	{
		for(ActiveBoxCollider collider : this.actives)
		{
			collider.onPreCollisionUpdate();
			for(GridCollider gridCollider : this.grids)
			{
				GridCollisionSolver.checkCollision(collider, gridCollider, collider::onCollision);
			}
			BoxCollisionSolver.checkCollision(collider, this.colliders, collider::onCollision, collider::canCollideWith);
			collider.onPostCollisionUpdate();
		}
	}

	public Iterable<BoxCollider> getColliders()
	{
		return this.colliders;
	}

	public Iterable<GridCollider> getGrids()
	{
		return this.grids;
	}
}
