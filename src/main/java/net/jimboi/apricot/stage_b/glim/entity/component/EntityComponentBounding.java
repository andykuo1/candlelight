package net.jimboi.apricot.stage_b.glim.entity.component;

import net.jimboi.apricot.base.entity.EntityComponent;
import net.jimboi.apricot.stage_b.glim.bounding.Bounding;

/**
 * Created by Andy on 6/4/17.
 */
public class EntityComponentBounding implements EntityComponent
{
	public Bounding bounding;

	public EntityComponentBounding(Bounding bounding)
	{
		this.bounding = bounding;
	}
}
