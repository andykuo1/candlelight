package boron.stage_a.goblet.component;

import boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;

import boron.bstone.entity.Component;

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
