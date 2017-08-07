package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/6/17.
 */
public class EntityBoulder extends SmackEntity
{
	public EntityBoulder(SmackWorld world, Transform3 transform, float size)
	{
		super(world, transform, size, world.createRenderable2D(transform, 'O', 0x888888));

		renderable.getRenderModel().transformation().scale(size * 1.25F, size * 1.25F, 1);
	}
}
