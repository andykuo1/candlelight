package net.jimboi.apricot.stage_c.hoob;

import net.jimboi.boron.stage_a.shroom.woot.collision.DynamicCollider;
import net.jimboi.boron.stage_a.shroom.woot.collision.Shape;

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
