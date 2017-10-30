package net.jimboi.canary.stage_a.cuplet.scene_main.entity.base;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.canary.stage_a.cuplet.scene_main.GobletWorld;
import net.jimboi.canary.stage_a.cuplet.scene_main.component.ComponentBounding;

import org.bstone.entity.EntityManager;
import org.bstone.transform.Transform3;

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
	public void onEntityCreate(EntityManager entityManager)
	{
		super.onEntityCreate(entityManager);

		this.addComponent(new ComponentBounding(this.boundingBox));
	}

	@Override
	public AxisAlignedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
