package net.jimboi.dood.component;

import net.jimboi.mod2.transform.DirectionVectors;

import org.qsilver.entity.Component;

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
