package canary.cuplet.scene_main.entity;

import canary.cuplet.scene_main.GobletWorld;

/**
 * Created by Andy on 8/10/17.
 */
@FunctionalInterface
public interface ExplosionFunction
{
	void execute(GobletWorld world, float x, float y);
}
