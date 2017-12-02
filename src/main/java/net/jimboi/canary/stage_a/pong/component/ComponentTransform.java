package net.jimboi.canary.stage_a.pong.component;

import org.bstone.entity.Component;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 12/2/17.
 */
public class ComponentTransform implements Component
{
	public final Transform3 transform;

	public ComponentTransform()
	{
		this(new Transform3());
	}

	public ComponentTransform(Transform3 transform)
	{
		this.transform = transform;
	}
}
