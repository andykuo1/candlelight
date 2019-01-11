package canary.cuplet.scene_main.entity.base;

import canary.base.collisionbox.box.AxisAlignedBoundingBox;
import canary.base.collisionbox.collider.BoxCollider;
import canary.cuplet.basicobject.ComponentRenderable;
import canary.cuplet.scene_main.GobletWorld;
import canary.cuplet.scene_main.component.ComponentBounding;

import canary.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class EntitySolid extends EntityBase implements BoxCollider
{
	protected final AxisAlignedBoundingBox boundingBox;

	public EntitySolid(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, ComponentRenderable renderable)
	{
		super(world, transform, renderable);

		this.boundingBox = boundingBox;
	}

	@Override
	protected void onEntitySetup()
	{
		super.onEntitySetup();

		this.addComponent(new ComponentBounding(this.boundingBox));
	}

	@Override
	public AxisAlignedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
