package apricot.stage_a.dood.component;

import apricot.stage_a.dood.entity.Component;

import apricot.bstone.transform.Transform3;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentTransform extends Component
{
	public final Transform3 transform;

	public ComponentTransform(Transform3 transform)
	{
		this.transform = transform;
	}
}
