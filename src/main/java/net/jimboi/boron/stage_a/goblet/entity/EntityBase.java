package net.jimboi.boron.stage_a.goblet.entity;

import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.goblet.GobletEntity;
import net.jimboi.boron.stage_a.goblet.GobletWorld;

import org.bstone.render.material.PropertyColor;
import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/11/17.
 */
public class EntityBase extends GobletEntity
{
	protected final int mainColor;

	public EntityBase(GobletWorld world, Transform3 transform, EntityComponentRenderable renderable)
	{
		super(world, transform, renderable);

		this.mainColor = PropertyColor.PROPERTY.getColor(this.renderable.getRenderModel().getMaterial());
	}
}
