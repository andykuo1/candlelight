package boron.zilar.dungeon;

import boron.base.gridmap.IntMap;

/**
 * Created by Andy on 5/28/17.
 */
public class DungeonData
{
	protected final IntMap tiles;
	protected final IntMap regions;

	public final int width;
	public final int height;

	public DungeonData(int width, int height)
	{
		this.width = width;
		this.height = height;

		this.tiles = new IntMap(width, height);
		this.regions = new IntMap(width, height);
	}

	public IntMap getRegions()
	{
		return this.regions;
	}

	public IntMap getTiles()
	{
		return this.tiles;
	}
}
