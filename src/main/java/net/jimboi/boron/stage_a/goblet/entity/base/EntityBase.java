package net.jimboi.boron.stage_a.goblet.entity.base;

import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.goblet.GobletEntity;
import net.jimboi.boron.stage_a.goblet.GobletWorld;
import net.jimboi.boron.stage_a.goblet.component.ComponentBounding;
import net.jimboi.boron.stage_a.goblet.component.ComponentTransform;

import org.bstone.entity.EntityManager;
import org.bstone.render.material.PropertyColor;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/11/17.
 */
public class EntityBase extends GobletEntity
{
	protected final int mainColor;

	public EntityBase(GobletWorld world, Transform3 transform, ComponentRenderable renderable)
	{
		super(world, transform, renderable);

		this.mainColor = PropertyColor.PROPERTY.getColor(this.renderable.getRenderModel().getMaterial());
	}

	@Override
	public void onEntityCreate(EntityManager entityManager)
	{
		super.onEntityCreate(entityManager);

		this.addComponent(new ComponentTransform(this.transform));
	}

	public void move(float dx, float dy)
	{
		this.transform.translate(dx, dy, 0);

		ComponentBounding componentBounding = this.getComponent(ComponentBounding.class);
		if (componentBounding != null)
		{
			componentBounding.getBoundingBox().offset(dx, dy);
		}
	}
}
