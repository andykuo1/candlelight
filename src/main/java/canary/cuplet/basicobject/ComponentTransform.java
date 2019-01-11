package canary.cuplet.basicobject;

import canary.bstone.entity.Component;
import canary.bstone.transform.Transform;

/**
 * Created by Andy on 8/12/17.
 */
public abstract class ComponentTransform implements Component
{
	public abstract Transform getTransform();
}
