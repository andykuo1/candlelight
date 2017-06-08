package net.jimboi.dood.component;

import net.jimboi.dood.entity.Component;
import net.jimboi.mod2.transform.Transform;

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
