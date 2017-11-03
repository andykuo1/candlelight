package net.jimboi.canary.stage_a.lantern.newglim.entity.component;

import net.jimboi.canary.stage_a.lantern.newglim.bounding.Bounding;

import org.zilar.entity.EntityComponent;

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
