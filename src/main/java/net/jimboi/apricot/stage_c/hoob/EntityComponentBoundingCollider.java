package net.jimboi.apricot.stage_c.hoob;

import org.zilar.collision.DynamicCollider;
import org.zilar.collision.Shape;
import org.zilar.entity.EntityComponent;

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
