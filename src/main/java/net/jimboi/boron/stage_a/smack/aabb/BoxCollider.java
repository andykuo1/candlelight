package net.jimboi.boron.stage_a.smack.aabb;

/**
 * Created by Andy on 8/7/17.
 */
public interface BoxCollider
{
	AxisAlignedBoundingBox getBoundingBox();

	default boolean isSolid()
	{
		return true;
	}
}
