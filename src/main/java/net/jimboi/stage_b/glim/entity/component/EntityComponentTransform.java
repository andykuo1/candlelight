package net.jimboi.stage_b.glim.entity.component;

import org.bstone.transform.Transform3;
import org.zilar.entity.EntityComponent;

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
