package net.jimboi.test.suger.dungeon;

import org.bstone.util.chunk.Chunk;
import org.bstone.util.grid.BooleanMap;
import org.bstone.util.grid.ByteMap;
import org.bstone.util.grid.IntMap;

/**
 * Created by Andy on 11/15/17.
 */
public class DungeonChunk extends Chunk
{
	public static final int CHUNK_SIZE = 16;

	public final BooleanMap solids;
	public final BooleanMap permeables;
	public final ByteMap directions;
	public final IntMap regions;
	public final IntMap items;

	public DungeonChunk(int chunkCoordX, int chunkCoordY, int chunkSize)
	{
		this(chunkCoordX, chunkCoordY, chunkSize,
				new BooleanMap(chunkSize, chunkSize),
				new BooleanMap(chunkSize, chunkSize),
				new ByteMap(chunkSize, chunkSize),
				new IntMap(chunkSize, chunkSize),
				new IntMap(chunkSize, chunkSize));

		this.solids.clear(true);
		this.directions.clear((byte) 15);
	}

	public DungeonChunk(int chunkCoordX, int chunkCoordY, int chunkSize,
	                    BooleanMap solids, BooleanMap permeables,
	                    ByteMap directions, IntMap regions, IntMap items)
	{
		super(chunkCoordX, chunkCoordY, chunkSize);

		this.solids = solids;
		this.permeables = permeables;
		this.directions = directions;
		this.regions = regions;
		this.items = items;
	}
}
