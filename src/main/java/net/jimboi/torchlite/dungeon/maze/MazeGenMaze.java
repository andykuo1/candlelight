package net.jimboi.torchlite.dungeon.maze;

import net.jimboi.torchlite.dungeon.DungeonData;

import org.bstone.util.map2d.IntMap;

import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by Andy on 5/29/17.
 */
public class MazeGenMaze extends AbstractMazeGen
{
	private RegionHandler regionHandler;
	private CorridorBuilder corridorBuilder;

	private float twistPercent;
	private Predicate<Integer> isCarveableTile;

	private IntMap src;
	private IntMap dst;

	public MazeGenMaze(RegionHandler regionHandler, CorridorBuilder corridorBuilder, IntMap tiles, float twistPercent, Predicate<Integer> isCarveableTile)
	{
		this.regionHandler = regionHandler;
		this.corridorBuilder = corridorBuilder;

		this.src = tiles;
		this.dst = new IntMap(this.src.width, this.src.height);

		this.twistPercent = twistPercent;
		this.isCarveableTile = isCarveableTile;
	}

	public void generate(Random rand)
	{
		for (int x = 1; x < this.src.width; x += 2)
		{
			for (int y = 1; y < this.src.height; y += 2)
			{
				if (!this.isCarveableTile.test(this.src.get(x, y))) continue;

				this.corridorBuilder.carve(rand, this.src, this.dst, x, y, this.twistPercent, this.isCarveableTile);
			}
		}
	}

	@Override
	public void bake(DungeonData data)
	{
		IntMap tiles = data.getTiles();
		IntMap regions = data.getRegions();

		IntMap.replace(this.dst, 0, 0, this.dst.width, this.dst.height, 1, (tile) -> tile == 1);
		IntMap.overlay(tiles, 0, 0, this.dst);

		IntMap.replace(this.dst, 0, 0, this.dst.width, this.dst.height, this.regionHandler.getNextAvailableRegion(), (tile) -> tile != 0);
		IntMap.overlay(regions, 0, 0, this.dst);

		this.clear();
	}

	@Override
	public void clear()
	{
		IntMap.fill(this.dst, 0, 0, this.dst.width, this.dst.height, 0);
	}
}
