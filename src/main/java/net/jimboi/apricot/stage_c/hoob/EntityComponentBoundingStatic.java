package net.jimboi.apricot.stage_c.hoob;

import org.zilar.collision.Shape;
import org.zilar.collision.StaticCollider;
import org.zilar.entity.EntityComponent;

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
