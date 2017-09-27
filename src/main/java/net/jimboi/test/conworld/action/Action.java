package net.jimboi.test.conworld.action;

import net.jimboi.test.conworld.acor.Actor;
import net.jimboi.test.conworld.world.World;

/**
 * Created by Andy on 8/29/17.
 */
public abstract class Action
{
	public abstract void execute(World world, Actor owner);

	public abstract int getActionPoints();

	public abstract String getName();
}
