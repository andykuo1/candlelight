package net.jimboi.stage_a.dood.component;

import net.jimboi.stage_a.dood.entity.Component;

import org.qsilver.transform.DirectionVectors;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentLocalDirection extends Component
{
	public DirectionVectors localDirectionVector;

	public ComponentLocalDirection(DirectionVectors localDirectionVector)
	{
		this.localDirectionVector = localDirectionVector;
	}
}
