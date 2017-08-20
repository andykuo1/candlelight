package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.box.AxisAlignedBoundingBox;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.component.ComponentMotion;

import org.bstone.entity.EntityManager;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class EntityMotion extends EntitySolid
{
	protected final ComponentMotion componentMotion;

	public EntityMotion(GobletWorld world, Transform3 transform, AxisAlignedBoundingBox boundingBox, ComponentRenderable renderable)
	{
		super(world, transform, boundingBox, renderable);

		this.componentMotion = new ComponentMotion();
	}

	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		super.onEntityCreate(entityManager);

		this.addComponent(this.componentMotion);
	}

	@Override
	public void onLivingLateUpdate()
	{
		super.onLivingLateUpdate();

		this.componentMotion.updateMotion();
		this.componentMotion.applyMotion(this.transform);

		if (this.boundingBox != null)
		{
			this.boundingBox.setCenter(this.transform.position3().x(), this.transform.position3().y());
		}
	}

	public void move(float dx, float dy)
	{
		this.transform.translate(dx, dy, 0);

		if (this.boundingBox != null)
		{
			this.boundingBox.offset(dx, dy);
		}
	}
}
