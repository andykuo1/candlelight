package canary.cuplet.scene_main.component;

import canary.base.collisionbox.box.AxisAlignedBoundingBox;

import canary.bstone.entity.Component;

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
