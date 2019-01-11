package boron.stage_a.goblet.component;

import boron.bstone.entity.Component;
import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 9/3/17.
 */
public class ComponentTransform implements Component
{
	private final Transform3 transform;

	public ComponentTransform(Transform3 transform)
	{
		this.transform = transform;
	}

	public final Transform3 getTransform()
	{
		return this.transform;
	}
}
