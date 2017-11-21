package net.jimboi.apricot.stage_c.hoob;

import net.jimboi.apricot.base.collision.DynamicCollider;
import net.jimboi.apricot.base.collision.Shape;
import net.jimboi.apricot.base.entity.EntityComponent;

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
