package net.jimboi.boron.stage_a.shroom.component;

import org.zilar.bounding.BoundingCollider;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/19/17.
 */
public class EntityComponentBounding implements EntityComponent
{
	public final BoundingCollider collider;

	public EntityComponentBounding(BoundingCollider collider)
	{
		this.collider = collider;
	}
}
