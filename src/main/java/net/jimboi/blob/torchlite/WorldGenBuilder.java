package net.jimboi.blob.torchlite;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WorldGenBuilder
{
	private WorldGenData worldData;
	private Random rand;

	public WorldGenBuilder()
	{

	}

	public WorldGenData bakeAndClear()
	{
		WorldGenData data = this.bake();
		this.clear();
		return data;
	}

	public WorldGenData bake()
	{
		return this.worldData;
	}

	public void clear()
	{
		this.worldData = null;
		this.rand = null;
	}

	public void create(Random rand, int width, int height, int cellSize)
	{
		this.worldData = cellSize == 0 ? new WorldGenData(width, height) : new WorldGenData(width, height, cellSize);
		this.rand = rand;
	}

	public void create(int width, int height, int cellSize)
	{
		this.worldData = cellSize == 0 ? new WorldGenData(width, height) : new WorldGenData(width, height, cellSize);
		this.rand = new Random();
	}

	public void fill(int tile)
	{
		WorldGenFill gen = new WorldGenFill(tile);
		gen.generate(this.rand, this.worldData);
	}

	public void generateMaze(Predicate<Integer> isCarveableTile, int floorTile, float twistPercent)
	{
		WorldGenMaze genMaze = new WorldGenMaze(isCarveableTile, floorTile, twistPercent);
		genMaze.generate(this.rand, this.worldData);
	}

	public void generateGrid(int gridTile, int gridSize, boolean alternate)
	{
		WorldGenGrid genGrid = new WorldGenGrid(gridTile, gridSize);
		genGrid.setAlternate(alternate);
		genGrid.generate(this.rand, this.worldData);
	}

	public void makeRegionMerges(Predicate<Integer> isCarveableTile, Supplier<Integer> getConnectorTiles, float mergePercent)
	{
		WorldGenRegionMerge genRegMerge = new WorldGenRegionMerge(isCarveableTile, getConnectorTiles, mergePercent);
		genRegMerge.generate(this.rand, this.worldData);
	}

	public void makeMazeSparse(int carveableTile)
	{
		WorldGenMazeSparse genMazeSparse = new WorldGenMazeSparse(carveableTile);
		genMazeSparse.generate(this.rand, this.worldData);
	}

	public boolean putRoom(int floorTile, Recti room, boolean checkIntersection)
	{
		WorldGenRoom genRoom = new WorldGenRoom(floorTile, room, this.worldData.regionIndex);
		genRoom.setCheckIntersection(checkIntersection);
		boolean result = genRoom.generate(this.rand, this.worldData);
		if (!result)
		{
			this.worldData.nextRegion();
		}
		return result;
	}

	public void putSpawnRoom(int spawnTile, int floorTile, Recti room)
	{
		WorldGenRoom genRoom = new WorldGenRoomSpawn(floorTile, spawnTile, room, this.worldData.regionIndex);
		genRoom.generate(this.rand, this.worldData);
		this.worldData.nextRegion();
	}

	public void putRandomSpawnRoom(int spawnTile, int floorTile, int minRoomSize, int maxRoomSize)
	{
		Recti room = this.getRandomRoom(minRoomSize, maxRoomSize);
		this.putSpawnRoom(spawnTile, floorTile, room);
	}

	public void putRandomRooms(int floorTile, int numAttempts, int minRoomSize, int maxRoomSize)
	{
		for(int i = 0; i < numAttempts; ++i)
		{
			Recti room = this.getRandomRoom(minRoomSize, maxRoomSize);
			if (!this.putRoom(floorTile, room, true))
			{
				continue;
			}
		}
	}

	public Recti getRandomRoom(int minRoomSize, int maxRoomSize)
	{
		int size = (minRoomSize + this.rand.nextInt(maxRoomSize)) * 2 + 1;
		int rectable = this.rand.nextInt(1 + size / 2) * 2;

		int width = size;
		int height = size;

		if (this.rand.nextBoolean())
		{
			width += rectable;
		}
		else
		{
			height += rectable;
		}

		int x = this.rand.nextInt((this.worldData.map.width - width) / 2) * 2 + 1;
		int y = this.rand.nextInt((this.worldData.map.height - height) / 2) * 2 + 1;

		return new Recti(x, y, width - 1, height - 1);
	}

	public Predicate<Integer> getIsTilePredicate(final int... tiles)
	{
		return new Predicate<Integer>(){
			@Override
			public boolean test(Integer t) { return ArrayUtil.indexOf(ArrayUtil.toInteger(tiles), t) != -1; }
		};
	}

	public Predicate<Integer> getIsTilePredicate(final int tile)
	{
		return new Predicate<Integer>(){
			@Override
			public boolean test(Integer t) { return t == tile; }
		};
	}

	public Supplier<Integer> getGetTileSupplier(final int... tiles)
	{
		return new Supplier<Integer>(){
			@Override
			public Integer get() { return tiles[rand.nextInt(tiles.length)]; }
		};
	}

	public Supplier<Integer> getGetTileSupplier(final int tile)
	{
		return new Supplier<Integer>(){
			@Override
			public Integer get() { return tile; }
		};
	}

	public void setRandom(Random rand)
	{
		this.rand = rand;
	}

	public Random getRandom()
	{
		return this.rand;
	}
}
