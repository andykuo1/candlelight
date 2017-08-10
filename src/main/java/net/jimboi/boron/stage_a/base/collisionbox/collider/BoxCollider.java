package net.jimboi.boron.stage_a.base.collisionbox.collider;

import net.jimboi.boron.stage_a.base.collisionbox.box.BoundingBox;

/**
 * Created by Andy on 8/7/17.
 */
public interface BoxCollider
{
	BoundingBox getBoundingBox();

	default boolean isSolid()
	{
		return true;
	}
}
