package net.jimboi.boron.stage_a.goblet;

import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletEntity extends LivingEntity
{
	protected final GobletWorld world;

	public GobletEntity(GobletWorld world, Transform3 transform, EntityComponentRenderable renderable)
	{
		super(transform, renderable);

		this.world = world;
	}

	public final GobletWorld getWorld()
	{
		return this.world;
	}
}
