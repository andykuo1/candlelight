package net.jimboi.boron.stage_a.shroom.component;

import net.jimboi.boron.stage_a.shroom.woot.collision.DynamicCollider;

import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/19/17.
 */
public class EntityComponentBounding implements EntityComponent
{
	public final DynamicCollider collider;

	public EntityComponentBounding(DynamicCollider collider)
	{
		this.collider = collider;
	}
}
