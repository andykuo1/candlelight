package net.jimboi.boron.stage_a.smack.aabb;

/**
 * Created by Andy on 8/7/17.
 */
public interface GridCollider extends BoxCollider
{
	GridBasedBoundingMap getGridBox();

	@Override
	GridAlignedBoundingBox getBoundingBox();
}
