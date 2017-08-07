package net.jimboi.boron.stage_a.smack.aabb;

/**
 * Created by Andy on 8/6/17.
 */
public interface BoundingBoxCollider
{
	AxisAlignedBoundingBox getBoundingBox();

	default boolean isSolid()
	{
		return true;
	}
}
