package boron.stage_a.goblet.entity;

import boron.stage_a.goblet.GobletWorld;

/**
 * Created by Andy on 8/10/17.
 */
@FunctionalInterface
public interface ExplosionFunction
{
	void execute(GobletWorld world, float x, float y);
}
