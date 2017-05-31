package net.jimboi.torchlite.dungeon;

import net.jimboi.torchlite.dungeon.maze.MazeDungeonBuilder;

import org.bstone.util.map2d.IntMap;

/**
 * Created by Andy on 5/28/17.
 */
public class Test
{
	public static void main(String[] args)
	{
		System.out.println("Generating World . . .");
		MazeDungeonBuilder mdb = new MazeDungeonBuilder(0, 45, 45);
		DungeonData dd = mdb.bake();
		IntMap map = dd.getTiles();

		System.out.println("OUTPUT:");
		for (int i = 0; i < map.height; ++i)
		{
			for (int j = 0; j < map.width; ++j)
			{
				int tile = map.get(j, i);
				System.out.print(tile == 0 ? " " : tile);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}
