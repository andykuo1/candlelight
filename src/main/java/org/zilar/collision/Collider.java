package org.zilar.collision;

import org.joml.Vector2f;

/**
 * Created by Andy on 7/21/17.
 */
public abstract class Collider
{
	protected CollisionManager collisionManager;
	protected final Shape shape;

	Collider(CollisionManager collisionManager, Shape shape)
	{
		this.collisionManager = collisionManager;
		this.shape = shape;
	}

	public void setPosition(float x, float y)
	{
		this.shape.setCenter(x, y);
	}

	public abstract Vector2f update(float x, float y, Vector2f dst);

	public final Shape getShape()
	{
		return this.shape;
	}

	public final CollisionManager getCollisionManager()
	{
		return this.collisionManager;
	}
}
