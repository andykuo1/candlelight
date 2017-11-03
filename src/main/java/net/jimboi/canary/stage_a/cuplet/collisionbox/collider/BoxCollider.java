package net.jimboi.canary.stage_a.cuplet.collisionbox.collider;

import net.jimboi.canary.stage_a.cuplet.collisionbox.box.BoundingBox;
import net.jimboi.canary.stage_a.cuplet.collisionbox.response.CollisionResponse;

/**
 * Created by Andy on 8/7/17.
 */
public interface BoxCollider
{
	default void onPreCollisionUpdate()
	{}

	default boolean onCollision(CollisionResponse collision)
	{
		return false;
	}

	default void onPostCollisionUpdate()
	{}

	default boolean canCollideWith(BoxCollider collider)
	{
		return false;
	}

	BoundingBox getBoundingBox();

	default boolean isSolid()
	{
		return true;
	}

	default boolean isColliderActive()
	{
		return false;
	}
}
