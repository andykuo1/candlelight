package apricot.stage_a.dood.component;

import apricot.stage_a.dood.entity.Component;

import apricot.bstone.transform.DirectionVector3;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentLocalDirection extends Component
{
	public DirectionVector3 localDirectionVector;

	public ComponentLocalDirection(DirectionVector3 localDirectionVector)
	{
		this.localDirectionVector = localDirectionVector;
	}
}
