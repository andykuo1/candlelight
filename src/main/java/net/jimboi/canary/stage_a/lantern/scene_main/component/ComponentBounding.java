package net.jimboi.canary.stage_a.lantern.scene_main.component;

import net.jimboi.canary.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.canary.stage_a.base.collisionbox.collider.BoxCollider;

import org.bstone.entity.Component;

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
