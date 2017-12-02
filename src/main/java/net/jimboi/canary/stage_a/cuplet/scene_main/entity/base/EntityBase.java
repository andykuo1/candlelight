package net.jimboi.canary.stage_a.cuplet.scene_main.entity.base;

import net.jimboi.canary.stage_a.base.MaterialProperty;
import net.jimboi.canary.stage_a.cuplet.basicobject.ComponentRenderable;
import net.jimboi.canary.stage_a.cuplet.scene_main.GobletEntity;
import net.jimboi.canary.stage_a.cuplet.scene_main.GobletWorld;
import net.jimboi.canary.stage_a.cuplet.scene_main.component.ComponentBounding;
import net.jimboi.canary.stage_a.cuplet.scene_main.component.ComponentTransform;

import org.bstone.entity.EntityManager;
import org.bstone.transform.Transform3;
import org.bstone.util.ColorUtil;

/**
 * Created by Andy on 8/11/17.
 */
public class EntityBase extends GobletEntity
{
	protected final int mainColor;

	public EntityBase(GobletWorld world, Transform3 transform, ComponentRenderable renderable)
	{
		super(world, transform, renderable);

		this.mainColor = ColorUtil.getColor(this.renderable.getRenderModel().material().getProperty(MaterialProperty.COLOR));
	}

	@Override
	protected void onEntitySetup(EntityManager entityManager)
	{
		super.onEntitySetup(entityManager);

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
