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
		super(chunkCoordX, chunkCoordY, chunkSize);

		this.solids = new BooleanMap(this.chunkSize, this.chunkSize);
		this.solids.clear(true);
		this.permeables = new BooleanMap(this.chunkSize, this.chunkSize);
		this.directions = new ByteMap(this.chunkSize, this.chunkSize);
		this.directions.clear((byte) 15);
		this.regions = new IntMap(this.chunkSize, this.chunkSize);
		this.items = new IntMap(this.chunkSize, this.chunkSize);
	}
}
