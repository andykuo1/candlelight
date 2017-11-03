package org.zilar.dungeon.maze;

import org.bstone.util.Direction;
import org.joml.Vector2i;
import org.zilar.dungeon.DungeonData;

import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Andy on 5/29/17.
 */
public class MazeGenSparseMaze extends AbstractMazeGen
{
	protected final Random rand;

	protected final Predicate<Integer> isCarveableTile;

	public MazeGenSparseMaze(Random rand, Predicate<Integer> isCarveableTile)
	{
		this.rand = rand;

		this.isCarveableTile = isCarveableTile;
	}

	@Override
	public void bake(DungeonData data)
	{
		boolean running = true;

		while (running)
		{
			running = false;

			for (int x = 1; x < data.width - 1; ++x)
			{
				for (int y = 1; y < data.height - 1; ++y)
				{
					if (this.isCarveableTile.test(data.getTiles().get(x, y))) continue;

					int exits = 0;
					for (Direction dir : Direction.Cardinals.values())
					{
						Vector2i dv = dir.polari(new Vector2i());
						if (!this.isCarveableTile.test(data.getTiles().get(x + dv.x(), y + dv.y())))
							exits++;
					}

					if (exits != 1) continue;

					data.getTiles().put(x, y, 0);
					running = true;
				}
			}
		}
	}

	@Override
	public void clear()
	{

	}
}
