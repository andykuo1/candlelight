package canary.test.conworld.action;

import canary.test.conworld.acor.Actor;
import canary.test.conworld.world.World;

/**
 * Created by Andy on 8/29/17.
 */
public abstract class Action
{
	public abstract void execute(World world, Actor owner);

	public abstract int getActionPoints();

	public abstract String getName();
}
