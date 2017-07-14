package net.jimboi.stage_a.dood.component;

import net.jimboi.stage_a.dood.entity.Component;

import org.bstone.transform.DirectionVector3;

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
