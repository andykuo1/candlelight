package net.jimboi.glim.component;

import net.jimboi.mod.transform.Transform;

import org.qsilver.entity.Component;

/**
 * Created by Andy on 6/1/17.
 */
public class EntityComponentTransform extends Component
{
	public final Transform transform;

	public EntityComponentTransform(Transform transform)
	{
		this.transform = transform;
	}

}
