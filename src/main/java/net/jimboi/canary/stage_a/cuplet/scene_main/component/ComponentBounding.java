package net.jimboi.canary.stage_a.cuplet.scene_main.component;

import net.jimboi.canary.stage_a.cuplet.collisionbox.box.AxisAlignedBoundingBox;

import org.bstone.entity.Component;

/**
 * Created by Andy on 9/3/17.
 */
public class ComponentBounding implements Component
{
	private final AxisAlignedBoundingBox boundingBox;

	public ComponentBounding(AxisAlignedBoundingBox boundingBox)
	{
		this.boundingBox = boundingBox;
	}

	public AxisAlignedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
