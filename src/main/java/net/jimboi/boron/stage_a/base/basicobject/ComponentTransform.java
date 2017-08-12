package net.jimboi.boron.stage_a.base.basicobject;

import org.bstone.entity.Component;
import org.bstone.transform.Transform;

/**
 * Created by Andy on 8/12/17.
 */
public abstract class ComponentTransform implements Component
{
	public abstract Transform getTransform();
}
