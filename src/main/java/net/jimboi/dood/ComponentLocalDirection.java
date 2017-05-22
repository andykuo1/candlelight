package net.jimboi.dood;

import net.jimboi.mod.transform.LocalDirectionVector;

import org.qsilver.entity.Component;

/**
 * Created by Andy on 5/22/17.
 */
public class ComponentLocalDirection extends Component
{
	public LocalDirectionVector localDirectionVector;

	public ComponentLocalDirection(LocalDirectionVector localDirectionVector)
	{
		this.localDirectionVector = localDirectionVector;
	}
}
