package boron.zilar.dungeon.maze;

import boron.base.OldMapUtil;
import boron.base.gridmap.IntMap;

import boron.zilar.dungeon.DungeonData;

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
		this.dst = new IntMap(this.src.getWidth(), this.src.getHeight());

		this.twistPercent = twistPercent;
		this.isCarveableTile = isCarveableTile;
	}

	public void generate(Random rand)
	{
		for (int x = 1; x < this.src.getWidth(); x += 2)
		{
			for (int y = 1; y < this.src.getHeight(); y += 2)
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

		OldMapUtil.replace(this.dst, 0, 0, this.dst.getWidth(), this.dst.getHeight(), 1, (tile) -> tile == 1);
		OldMapUtil.overlay(tiles, 0, 0, this.dst);

		OldMapUtil.replace(this.dst, 0, 0, this.dst.getWidth(), this.dst.getHeight(), this.regionHandler.getNextAvailableRegion(), (tile) -> tile != 0);
		OldMapUtil.overlay(regions, 0, 0, this.dst);

		this.clear();
	}

	@Override
	public void clear()
	{
		this.dst.clear(0);
	}
}
