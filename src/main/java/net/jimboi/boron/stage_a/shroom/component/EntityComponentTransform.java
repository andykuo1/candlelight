package net.jimboi.boron.stage_a.shroom.component;

import org.bstone.transform.Transform;
import org.zilar.entity.EntityComponent;

/**
 * Created by Andy on 7/18/17.
 */
public class EntityComponentTransform implements EntityComponent
{
	public final Transform transform;

	public EntityComponentTransform(Transform transform)
	{
		this.transform = transform;
	}
}
