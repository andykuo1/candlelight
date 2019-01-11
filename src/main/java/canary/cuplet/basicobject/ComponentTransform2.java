package canary.cuplet.basicobject;

import canary.bstone.transform.Transform;
import canary.bstone.transform.Transform2;

/**
 * Created by Andy on 8/12/17.
 */
public class ComponentTransform2 extends ComponentTransform
{
	public Transform2 transform;

	public ComponentTransform2(Transform2 transform)
	{
		this.transform = transform;
	}

	@Override
	public Transform getTransform()
	{
		return this.transform;
	}
}
