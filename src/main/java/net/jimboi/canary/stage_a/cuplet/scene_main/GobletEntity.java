package net.jimboi.canary.stage_a.cuplet.scene_main;

import net.jimboi.canary.stage_a.cuplet.basicobject.ComponentRenderable;
import net.jimboi.canary.stage_a.cuplet.basicobject.LivingEntityBase;

import org.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletEntity extends LivingEntityBase
{
	protected final GobletWorld world;

	public GobletEntity(GobletWorld world, Transform3 transform, ComponentRenderable renderable)
	{
		super(transform, renderable);

		this.world = world;
	}

	public final GobletWorld getWorld()
	{
		return this.world;
	}
}
