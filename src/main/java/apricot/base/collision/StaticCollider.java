package apricot.base.collision;

import org.joml.Vector2f;

/**
 * Created by Andy on 7/20/17.
 */
public class StaticCollider extends Collider
{
	StaticCollider(CollisionManager collisionManager, Shape shape)
	{
		super(collisionManager, shape);
	}

	@Override
	public Vector2f update(float x, float y, Vector2f dst)
	{
		this.shape.setCenter(x, y);

		CollisionData data = this.collisionManager.getCollisionSolver().checkNearestCollision(this.shape);
		if (data != null)
		{
			dst.add(data.deltaX, data.deltaY);
			this.shape.setCenter(x + data.deltaX, y + data.deltaY);
		}

		return dst;
	}
}
