package boron.stage_a.goblet;

import boron.stage_a.base.basicobject.ComponentRenderable;
import boron.stage_a.base.basicobject.LivingEntityBase;

import boron.bstone.transform.Transform3;

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
