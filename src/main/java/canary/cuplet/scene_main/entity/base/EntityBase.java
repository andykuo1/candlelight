package canary.cuplet.scene_main.entity.base;

import canary.base.MaterialProperty;
import canary.cuplet.basicobject.ComponentRenderable;
import canary.cuplet.scene_main.GobletEntity;
import canary.cuplet.scene_main.GobletWorld;
import canary.cuplet.scene_main.component.ComponentBounding;
import canary.cuplet.scene_main.component.ComponentTransform;

import canary.bstone.transform.Transform3;
import canary.bstone.util.ColorUtil;

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
	protected void onEntitySetup()
	{
		super.onEntitySetup();

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
