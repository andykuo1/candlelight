package boron.stage_a.goblet.entity.base;

import boron.base.render.material.PropertyColor;
import boron.stage_a.base.basicobject.ComponentRenderable;
import boron.stage_a.goblet.GobletEntity;
import boron.stage_a.goblet.GobletWorld;
import boron.stage_a.goblet.component.ComponentBounding;
import boron.stage_a.goblet.component.ComponentTransform;

import boron.bstone.entity.EntityManager;
import boron.bstone.transform.Transform3;

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
