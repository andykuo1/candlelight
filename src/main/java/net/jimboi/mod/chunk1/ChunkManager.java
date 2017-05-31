package net.jimboi.mod.chunk1;

import java.util.HashMap;

/**
 * Created by Andy on 5/28/17.
 */
public class ChunkManager
{
	private HashMap<Integer, Chunk> chunkMap = new HashMap<>();

	public Chunk loadChunk(int chunkPos, long seed)
	{
		Chunk chunk;
		this.chunkMap.put(chunkPos, chunk = new Chunk());
		chunk.generate(seed);
		return chunk;
	}

	public Chunk getChunk(int chunkPos)
	{
		Chunk chunk = this.chunkMap.get(chunkPos);
		if (chunk == null)
		{
			chunk = this.loadChunk(chunkPos, chunkPos);
		}
		return chunk;
	}

	public Chunk getChunk(float posX)
	{
		int chunkPos = this.getChunkPos(posX);
		return this.getChunk(chunkPos);
	}

	public int getChunkPos(float posX)
	{
		return (int) (posX / Chunk.CHUNK_SIZE);
	}
}
