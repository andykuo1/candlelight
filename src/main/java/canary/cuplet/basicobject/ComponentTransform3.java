package canary.cuplet.basicobject;

import canary.bstone.transform.Transform;
import canary.bstone.transform.Transform3;

/**
 * Created by Andy on 8/12/17.
 */
public class ComponentTransform3 extends ComponentTransform
{
	public Transform3 transform;

	public ComponentTransform3(Transform3 transform)
	{
		this.transform = transform;
	}

	@Override
	public Transform getTransform()
	{
		return this.transform;
	}
}
