package net.jimboi.boron.stage_a.goblet.component;

import net.jimboi.boron.stage_a.base.collisionbox.box.BoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;

import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityComponentCollidable implements EntityComponent, BoxCollider
{
	public BoundingBox boundingBox;

	public EntityComponentCollidable(BoundingBox boundingBox)
	{
		this.boundingBox = boundingBox;
	}

	@Override
	public BoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
