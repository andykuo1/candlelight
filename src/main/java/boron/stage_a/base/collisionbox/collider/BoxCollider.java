package boron.stage_a.base.collisionbox.collider;

import boron.stage_a.base.collisionbox.box.BoundingBox;
import boron.stage_a.base.collisionbox.response.CollisionResponse;

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
