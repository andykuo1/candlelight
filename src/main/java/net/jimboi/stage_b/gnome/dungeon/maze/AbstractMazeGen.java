package net.jimboi.stage_b.gnome.dungeon.maze;

import net.jimboi.stage_b.gnome.dungeon.DungeonData;

/**
 * Created by Andy on 5/29/17.
 */
public abstract class AbstractMazeGen
{
	public abstract void bake(DungeonData data);

	public abstract void clear();
}
