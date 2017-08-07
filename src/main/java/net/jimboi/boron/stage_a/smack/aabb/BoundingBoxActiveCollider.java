package net.jimboi.boron.stage_a.smack.aabb;

import java.util.List;

/**
 * Created by Andy on 8/6/17.
 */
public interface BoundingBoxActiveCollider extends BoundingBoxCollider
{
	void onCheckIntersection(List<IntersectionData> intersections);

	boolean canCollideWith(BoundingBoxCollider collider);
}
