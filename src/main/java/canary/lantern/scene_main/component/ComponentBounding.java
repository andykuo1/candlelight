package canary.lantern.scene_main.component;

import canary.base.collisionbox.box.AxisAlignedBoundingBox;
import canary.base.collisionbox.collider.BoxCollider;

import canary.bstone.entity.Component;

/**
 * Created by Andy on 11/3/17.
 */
public class ComponentBounding implements Component, BoxCollider
{
	public final AxisAlignedBoundingBox boundingBox;

	public ComponentBounding(AxisAlignedBoundingBox boundingBox)
	{
		this.boundingBox = boundingBox;
	}

	@Override
	public final AxisAlignedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
