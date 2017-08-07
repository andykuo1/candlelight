package net.jimboi.boron.stage_a.smack.aabb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by Andy on 8/6/17.
 */
public class BoundingBoxManager
{
	private final Set<BoundingBoxCollider> boundings = new HashSet<>();
	private final Set<BoundingBoxActiveCollider> actives = new HashSet<>();

	public void add(BoundingBoxCollider collider)
	{
		this.boundings.add(collider);
		if (collider instanceof BoundingBoxActiveCollider)
		{
			this.actives.add((BoundingBoxActiveCollider) collider);
		}
	}

	public void remove(BoundingBoxCollider collider)
	{
		this.boundings.remove(collider);
		if (collider instanceof BoundingBoxActiveCollider)
		{
			this.actives.remove(collider);
		}
	}

	public void update()
	{
		List<IntersectionData> intersections = new ArrayList<>();
		for(BoundingBoxActiveCollider collider : this.actives)
		{
			this.checkIntersection(collider, intersections);
			if (!intersections.isEmpty())
			{
				collider.onCheckIntersection(intersections);
				intersections.clear();
			}
		}
	}

	public IntersectionData checkSweepIntersection(BoundingBoxActiveCollider collider, float dx, float dy)
	{
		IntersectionData data = new IntersectionData();
		for(BoundingBoxCollider other : this.boundings)
		{
			if (collider == other) continue;
			if (!other.isSolid()) continue;
			if (other.getBoundingBox() == null) continue;

			if (collider.canCollideWith(other))
			{
				AxisAlignedBoundingBox cbox = collider.getBoundingBox();
				AxisAlignedBoundingBox obox = other.getBoundingBox();

				if (AxisAlignedBoundingBox.testSweep(cbox, obox, dx, dy, data) != null)
				{
					data.collider = other;
					return data;
				}
			}
		}
		return null;
	}

	public IntersectionData checkFirstIntersection(BoundingBoxCollider collider, Predicate<BoundingBoxCollider> canCollide)
	{
		IntersectionData data = new IntersectionData();
		for(BoundingBoxCollider other : this.boundings)
		{
			if (collider == other) continue;
			if (!other.isSolid()) continue;
			if (other.getBoundingBox() == null) continue;

			if (canCollide.test(other))
			{
				AxisAlignedBoundingBox cbox = collider.getBoundingBox();
				AxisAlignedBoundingBox obox = other.getBoundingBox();

				if (AxisAlignedBoundingBox.test(cbox, obox, data) != null)
				{
					data.collider = other;
					return data;
				}
			}
		}
		return null;
	}

	public List<IntersectionData> checkIntersection(BoundingBoxActiveCollider collider, List<IntersectionData> dst)
	{
		IntersectionData data = new IntersectionData();
		for(BoundingBoxCollider other : this.boundings)
		{
			if (collider == other) continue;
			if (!other.isSolid()) continue;
			if (other.getBoundingBox() == null) continue;

			if (collider.canCollideWith(other))
			{
				AxisAlignedBoundingBox cbox = collider.getBoundingBox();
				AxisAlignedBoundingBox obox = other.getBoundingBox();

				if (AxisAlignedBoundingBox.test(cbox, obox, data) != null)
				{
					data.collider = other;
					dst.add(data);
					data = new IntersectionData();
				}
			}
		}
		return dst;
	}

	public Iterator<BoundingBoxCollider> getColliderIterator()
	{
		return this.boundings.iterator();
	}
}
