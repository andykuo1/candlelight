package boron.stage_a.base.basicobject;

import boron.bstone.entity.Component;
import boron.bstone.transform.Transform;

/**
 * Created by Andy on 8/12/17.
 */
public abstract class ComponentTransform implements Component
{
	public abstract Transform getTransform();
}
