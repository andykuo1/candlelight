package net.jimboi.canary.stage_a.cuplet.scene_main.component;

import org.bstone.entity.Component;
import org.bstone.transform.Transform3;

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
