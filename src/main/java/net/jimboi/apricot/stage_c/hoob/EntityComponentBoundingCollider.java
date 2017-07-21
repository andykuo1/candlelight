package net.jimboi.apricot.stage_c.hoob;

import net.jimboi.apricot.stage_c.hoob.collision.Collider;
import net.jimboi.apricot.stage_c.hoob.collision.Shape;

import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/15/17.
 */
public class EntityComponentBoundingCollider implements EntityComponent
{
	Collider collider;

	public Shape shape;

	public EntityComponentBoundingCollider(Shape shape)
	{
		this.shape = shape;
	}

	public Collider getCollider()
	{
		return this.collider;
	}
}
