package canary.cuplet.scene_main;

import canary.cuplet.basicobject.ComponentRenderable;
import canary.cuplet.basicobject.GameObjectBase;

import canary.bstone.transform.Transform3;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletEntity extends GameObjectBase
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
