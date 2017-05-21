package net.jimboi.mod.chunk;

import net.jimboi.mod.chunk.tileentity.TileEntity;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 5/13/17.
 */
public class ChunkData
{
	private final ChunkMap chunkMap;
	private final List<TileEntity> tileEntities;

	ChunkData(ChunkMap chunkMap, List<TileEntity> tileEntities)
	{
		this.chunkMap = chunkMap;
		this.tileEntities = tileEntities;
	}

	public ChunkMap getChunkMap()
	{
		return this.chunkMap;
	}

	public Iterator<TileEntity> getTileEntities()
	{
		return this.tileEntities.iterator();
	}
}
