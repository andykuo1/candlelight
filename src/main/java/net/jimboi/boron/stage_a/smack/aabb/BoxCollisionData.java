package net.jimboi.boron.stage_a.smack.aabb;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 8/7/17.
 */
public final class BoxCollisionData
{
	BoxCollider collider;
	final Vector2f delta = new Vector2f();
	final Vector2f point = new Vector2f();
	final Vector2f normal = new Vector2f();

	public Vector2fc getDelta()
	{
		return this.delta;
	}

	public Vector2fc getPoint()
	{
		return this.point;
	}

	public Vector2fc getNormal()
	{
		return this.normal;
	}

	public BoxCollider getCollider()
	{
		return this.collider;
	}

	public void set(BoxCollisionData data)
	{
		this.delta.set(data.delta);
		this.normal.set(data.normal);
		this.point.set(data.point);
	}
}
