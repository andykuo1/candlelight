package net.jimboi.mod.chunk;

import net.jimboi.mod.chunk.tileentity.TileEntity;

import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 5/13/17.
 */
public class ChunkLoader
{
	public Chunk load(Vector2ic chunkPos)
	{
		List<TileEntity> tileEntities = new ArrayList<>();

		int[] tilemap = new int[256];
		byte[] tiledata = new byte[256];

		for(int i = 0; i < tilemap.length; ++i)
		{
			tilemap[i] = (int)(Math.random() * 3);
		}

		int[] blockmap = new int[256];
		byte[] blockdata = new byte[256];

		for(int i = 0; i < blockmap.length; ++i)
		{
			blockmap[i] = (int)(Math.random() * 3);
		}

		ChunkMap chunkMap = new ChunkMap(blockmap, blockdata, tilemap, tiledata);

		ChunkData chunkData = new ChunkData(chunkMap, tileEntities);

		return new Chunk(chunkData, chunkPos.x(), chunkPos.y());
	}
}
