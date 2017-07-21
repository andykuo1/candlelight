package net.jimboi.apricot.stage_c.hoob.collision;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/20/17.
 */
public class Collider
{
	CollisionManager collisionManager;
	private final Shape shape;

	private final Vector2f velocity = new Vector2f();

	Collider(CollisionManager collisionManager, Shape shape)
	{
		this.collisionManager = collisionManager;
		this.shape = shape;
	}

	public void move(float x, float y)
	{
		this.velocity.add(x, y);
	}

	public void move(Vector2fc dv)
	{
		this.velocity.add(dv);
	}

	public void move(Vector2fc dir, float mag)
	{
		this.velocity.add(dir.x() * mag, dir.y() * mag);
	}

	public void setPosition(float x, float y)
	{
		this.shape.setCenter(x, y);
		this.velocity.set(0);
	}

	public boolean update(Vector2f position)
	{
		this.shape.setCenter(position.x(), position.y());

		CollisionData data = this.collisionManager.checkCollision(this.shape, this.velocity);
		if (data != null)
		{
			//Smooth Wall Sliding
			Vector2f vec = this.velocity;
			vec.set(data.deltaX, data.deltaY);
			float len = vec.length();
			vec.set(data.normalX, data.normalY).mul(len);
			data.deltaX -= vec.x();
			data.deltaY -= vec.y();

			position.add(data.deltaX, data.deltaY);
			this.shape.setCenter(position.x(), position.y());
			this.velocity.set(0);
			return true;
		}

		position.add(this.velocity);
		this.shape.setCenter(position.x(), position.y());
		this.velocity.set(0);
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

	public final Vector2fc getVelocity()
	{
		return this.velocity;
	}
}
