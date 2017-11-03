package net.jimboi.canary.stage_a.cuplet.collisionbox.response;

import net.jimboi.canary.stage_a.cuplet.collisionbox.collider.BoxCollider;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 8/7/17.
 */
public final class CollisionResponse
{
	public BoxCollider collider;
	public final Vector2f delta = new Vector2f();
	public final Vector2f point = new Vector2f();
	public final Vector2f normal = new Vector2f();

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

	public void set(CollisionResponse data)
	{
		this.delta.set(data.delta);
		this.normal.set(data.normal);
		this.point.set(data.point);
	}
}
