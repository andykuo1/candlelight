package net.jimboi.glim.component;

import net.jimboi.glim.bounding.Bounding;

import org.qsilver.entity.Component;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityComponentBounding extends Component
{
	public Bounding bounding;

	public EntityComponentBounding(Bounding bounding)
	{
		this.bounding = bounding;
	}
}
