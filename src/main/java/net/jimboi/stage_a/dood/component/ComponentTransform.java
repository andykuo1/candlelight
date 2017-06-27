package net.jimboi.stage_a.dood.component;

import net.jimboi.stage_a.dood.entity.Component;

import org.qsilver.transform.Transform;

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
