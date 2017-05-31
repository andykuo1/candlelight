package net.jimboi.torchlite.dungeon.maze;

import net.jimboi.torchlite.dungeon.DungeonData;

/**
 * Created by Andy on 5/29/17.
 */
public abstract class AbstractMazeGen
{
	public abstract void bake(DungeonData data);

	public abstract void clear();
}
