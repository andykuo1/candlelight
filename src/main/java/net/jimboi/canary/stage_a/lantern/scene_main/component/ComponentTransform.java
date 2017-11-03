package net.jimboi.canary.stage_a.lantern.scene_main.component;

import org.bstone.entity.Component;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 11/3/17.
 */
public class ComponentTransform implements Component
{
	public final Transform3 transform;

	public ComponentTransform(Transform3 transform)
	{
		this.transform = transform;
	}
}
