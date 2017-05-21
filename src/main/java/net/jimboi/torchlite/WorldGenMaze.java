package net.jimboi.torchlite;

import java.util.Random;
import java.util.function.Predicate;

public class WorldGenMaze extends AbstractWorldGen
{
	protected final Predicate<Integer> isCarveableTile;
	protected final int floorTile;
	protected final float twistPercent;

	public WorldGenMaze(Predicate<Integer> isSolidTile, int floorTile, float twistPercent)
	{
		this.isCarveableTile = isSolidTile;
		this.floorTile = floorTile;
		this.twistPercent = twistPercent;
	}

	@Override
	public boolean generate(Random rand, WorldGenData world)
	{
		for(int x = 1; x < world.map.width; x += 2)
		{
			for(int y = 1; y < world.map.height; y += 2)
			{
				Vec2i pos = new Vec2i(x, y);
				if (!this.isCarveableTile.test(world.map.get(x, y))) continue;

				WorldGenMazeCorridor genMazeCorridor = new WorldGenMazeCorridor(this.isCarveableTile, this.floorTile, world.nextRegion(), pos, this.twistPercent);
				genMazeCorridor.generate(rand, world);
			}
		}

		return true;
	}

	@Override
	public int getWeight()
	{
		return 0;
	}

}
