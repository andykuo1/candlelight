package net.jimboi.dood.component;

import net.jimboi.mod.transform.Transform;

import org.qsilver.entity.Component;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentTransform extends Component
{
	public final Transform transform;

	public ComponentTransform(Transform transform)
	{
		this.transform = transform;
	}
}
