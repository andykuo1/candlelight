package net.jimboi.boron.stage_a.smack.aabb;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 8/6/17.
 */
public final class IntersectionData
{
	BoundingBoxCollider collider = null;
	final Vector2f delta = new Vector2f();
	final Vector2f point = new Vector2f();
	final Vector2f normal = new Vector2f();

	public final BoundingBoxCollider getCollider()
	{
		return this.collider;
	}

	public final Vector2fc getDelta()
	{
		return this.delta;
	}

	public final Vector2fc getPoint()
	{
		return this.point;
	}

	public final Vector2fc getNormal()
	{
		return this.normal;
	}
}
