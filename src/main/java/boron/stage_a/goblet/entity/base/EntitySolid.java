package boron.stage_a.goblet.entity.base;

import boron.stage_a.base.basicobject.ComponentRenderable;
import boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import boron.stage_a.base.collisionbox.collider.BoxCollider;
import boron.stage_a.goblet.GobletWorld;
import boron.stage_a.goblet.component.ComponentBounding;

import boron.bstone.entity.EntityManager;
import boron.bstone.transform.Transform3;

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
