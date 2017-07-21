package net.jimboi.boron.stage_a.shroom.component;

import net.jimboi.apricot.stage_c.hoob.collision.Collider;

import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/19/17.
 */
public class EntityComponentBounding implements EntityComponent
{
	public final Collider collider;

	public EntityComponentBounding(Collider collider)
	{
		this.collider = collider;
	}
}
