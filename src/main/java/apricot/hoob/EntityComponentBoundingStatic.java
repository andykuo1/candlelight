package apricot.hoob;

import apricot.base.collision.Shape;
import apricot.base.collision.StaticCollider;
import apricot.base.entity.EntityComponent;

/**
 * Created by Andy on 7/15/17.
 */
public class EntityComponentBoundingStatic implements EntityComponent
{
	StaticCollider collider;

	public Shape shape;

	public EntityComponentBoundingStatic(Shape shape)
	{
		this.shape = shape;
	}

	public StaticCollider getCollider()
	{
		return this.collider;
	}
}
