package net.jimboi.boron.stage_a.smack.aabb;

/**
 * Created by Andy on 8/7/17.
 */
public interface ActiveBoxCollider extends BoxCollider
{
	void onPreCollisionUpdate();

	void onCollision(BoxCollisionData collision);

	void onPostCollisionUpdate();

	boolean canCollideWith(BoxCollider collider);
}
