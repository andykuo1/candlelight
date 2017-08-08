package net.jimboi.boron.stage_a.smack.collisionbox.collider;

import net.jimboi.boron.stage_a.smack.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.smack.collisionbox.response.CollisionResponse;

/**
 * Created by Andy on 8/7/17.
 */
public interface ActiveBoxCollider extends BoxCollider
{
	void onPreCollisionUpdate();

	boolean onCollision(CollisionResponse collision);

	void onPostCollisionUpdate();

	boolean canCollideWith(BoxCollider collider);

	@Override
	AxisAlignedBoundingBox getBoundingBox();
}
