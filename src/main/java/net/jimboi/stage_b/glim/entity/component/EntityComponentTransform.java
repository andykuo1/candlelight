package net.jimboi.stage_b.glim.entity.component;

import org.qsilver.entity.EntityComponent;
import org.zilar.transform.Transform3;

/**
 * Created by Andy on 6/1/17.
 */
public class EntityComponentTransform extends EntityComponent
{
	public final Transform3 transform;

	public EntityComponentTransform(Transform3 transform)
	{
		this.transform = transform;
	}
}
