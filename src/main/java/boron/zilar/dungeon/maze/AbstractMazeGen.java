package boron.zilar.dungeon.maze;

import boron.zilar.dungeon.DungeonData;

/**
 * Created by Andy on 5/29/17.
 */
public abstract class AbstractMazeGen
{
	public abstract void bake(DungeonData data);

	public abstract void clear();
}
