package org.zilar.collision;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 7/20/17.
 */
public class DynamicCollider extends Collider
{
	protected final Vector2f velocity = new Vector2f();

	DynamicCollider(CollisionManager collisionManager, Shape shape)
	{
		super(collisionManager, shape);
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

	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		this.velocity.set(0);
	}

	@Override
	public Vector2f update(float x, float y, Vector2f dst)
	{
		this.shape.setCenter(x, y);

		if (this.velocity.x() == 0 && this.velocity.y() == 0)
		{
			CollisionData data = this.collisionManager.getCollisionSolver().checkNearestCollision(this.shape);
			if (data != null)
			{
				dst.add(data.deltaX, data.deltaY);
				this.shape.setCenter(x + data.deltaX, y + data.deltaY);
			}
		}
		else
		{
			CollisionData data = this.collisionManager.getCollisionSolver().checkCollision(this.shape, this.velocity);
			if (data != null)
			{
				//Smooth Wall Sliding
				Vector2f vec = this.velocity;
				vec.set(data.deltaX, data.deltaY);
				float len = vec.length();
				vec.set(data.normalX, data.normalY).mul(len);
				data.deltaX -= vec.x();
				data.deltaY -= vec.y();

				dst.add(data.deltaX, data.deltaY);
				this.shape.setCenter(x + data.deltaX, y + data.deltaY);
				this.velocity.set(0);
			}
			else
			{
				dst.add(this.velocity);
				this.shape.setCenter(x + this.velocity.x(), y + this.velocity.y());
				this.velocity.set(0);
			}
		}

		return dst;
	}

	public final Vector2fc getVelocity()
	{
		return this.velocity;
	}
}
