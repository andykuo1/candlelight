package net.jimboi.apricot.stage_c.hoob.collision;

import org.joml.Vector2f;

/**
 * Created by Andy on 7/20/17.
 */
public class StaticCollider
{
	CollisionManager collisionManager;
	private final Shape shape;

	StaticCollider(CollisionManager collisionManager, Shape shape)
	{
		this.collisionManager = collisionManager;
		this.shape = shape;
	}

	public void setPosition(float x, float y)
	{
		this.shape.setCenter(x, y);
	}

	public boolean update(Vector2f position)
	{
		this.shape.setCenter(position.x(), position.y());

		CollisionData data = this.collisionManager.checkCollision(this.shape);
		if (data != null)
		{
			position.add(data.deltaX, data.deltaY);
			this.shape.setCenter(position.x(), position.y());
			return true;
		}

		return false;
	}

	public final Shape getShape()
	{
		return this.shape;
	}

	public final CollisionManager getCollisionManager()
	{
		return this.collisionManager;
	}
}
