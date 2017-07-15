package net.jimboi.stage_c.hoob;

import org.qsilver.entity.EntityComponent;
import org.zilar.bounding.BoundingCollider;
import org.zilar.bounding.Shape;

/**
 * Created by Andy on 7/15/17.
 */
public class EntityComponentBoundingCollider implements EntityComponent
{
	protected BoundingCollider bounding;
	public Shape shape;

	public EntityComponentBoundingCollider(Shape shape)
	{
		this.shape = shape;
	}

	public BoundingCollider getBounding()
	{
		return this.bounding;
	}
}
