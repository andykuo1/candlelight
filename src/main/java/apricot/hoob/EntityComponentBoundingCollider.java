package apricot.hoob;

import apricot.base.collision.DynamicCollider;
import apricot.base.collision.Shape;
import apricot.base.entity.EntityComponent;

/**
 * Created by Andy on 7/15/17.
 */
public class EntityComponentBoundingCollider implements EntityComponent
{
	DynamicCollider collider;

	public Shape shape;

	public EntityComponentBoundingCollider(Shape shape)
	{
		this.shape = shape;
	}

	public DynamicCollider getCollider()
	{
		return this.collider;
	}
}
