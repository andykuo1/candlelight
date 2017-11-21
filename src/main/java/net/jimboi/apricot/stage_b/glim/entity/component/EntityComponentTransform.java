package net.jimboi.apricot.stage_b.glim.entity.component;

import net.jimboi.apricot.base.entity.EntityComponent;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 6/1/17.
 */
public class EntityComponentTransform implements EntityComponent
{
	public final Transform3 transform;

	public EntityComponentTransform(Transform3 transform)
	{
		this.transform = transform;
	}
}
