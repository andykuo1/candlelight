package net.jimboi.torchlite;

import java.util.ArrayList;
import java.util.List;

public class WorldGenData
{
	public final int mapWidth;
	public final int mapHeight;
	public final int mapCellSize;

	public final IntMap map;
	public final IntMap region;
	protected int regionIndex = 0;

	public final List<Recti> rooms = new ArrayList<>();
	public final List<Vec2i> spawns = new ArrayList<>();

	protected WorldGenData(int width, int height, int cellSize)
	{
		Log.ASSERT(width % cellSize == 0);
		Log.ASSERT(height % cellSize == 0);

		this.mapWidth = width;
		this.mapHeight = height;
		this.mapCellSize = cellSize;

		this.map = new IntMultiMap(this.mapWidth, this.mapHeight, this.mapCellSize);
		this.region = new IntMultiMap(this.mapWidth, this.mapHeight, this.mapCellSize);
	}

	protected WorldGenData(int width, int height)
	{
		this.mapWidth = width;
		this.mapHeight = height;
		this.mapCellSize = 0;

		this.map = new IntMap(this.mapWidth, this.mapHeight);
		this.region = new IntMap(this.mapWidth, this.mapHeight);
	}

	protected int nextRegion()
	{
		return this.regionIndex++;
	}

	public int getWidth()
	{
		return this.mapCellSize == 0 ? 1 : this.map.width / this.mapCellSize;
	}

	public int getHeight()
	{
		return this.mapCellSize == 0 ? 1 : this.map.height / this.mapCellSize;
	}

	public IntMultiMap getMultiMap()
	{
		Log.ASSERT(this.isMultiMap());

		return (IntMultiMap) this.map;
	}

	public IntMultiMap getMultiRegion()
	{
		Log.ASSERT(this.isMultiMap());

		return (IntMultiMap) this.region;
	}

	public boolean isMultiMap()
	{
		return this.mapCellSize != 0;
	}
}
