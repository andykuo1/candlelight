package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class EntitySolid extends EntityBase implements BoxCollider
{
	protected final AxisAlignedBoundingBox boundingBox;

	public EntitySolid(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, EntityComponentRenderable renderable)
	{
		super(world, transform, renderable);

		this.boundingBox = boundingBox;
	}

	@Override
	public AxisAlignedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
