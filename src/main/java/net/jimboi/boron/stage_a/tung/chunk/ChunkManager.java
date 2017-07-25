package net.jimboi.boron.stage_a.tung.chunk;

import org.joml.Vector2ic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 5/13/17.
 */
public class ChunkManager
{
	private Map<Vector2ic, Chunk> activeChunks = new HashMap<>();
	private ChunkLoader chunkLoader;

	public ChunkManager()
	{
		this.chunkLoader = new ChunkLoader();
	}

	public ChunkManager setChunkLoader(ChunkLoader chunkLoader)
	{
		this.chunkLoader = chunkLoader;
		return this;
	}

	public void update()
	{
		for(Chunk chunk : this.activeChunks.values())
		{
			//Update chunk
			if (chunk.dirty)
			{
				this.onChunkUpdate(chunk);
				chunk.activeTime = this.getCurrentTime();
				chunk.dirty = false;
			}
		}
	}

	public void onChunkUpdate(Chunk chunk)
	{

	}

	public Chunk getChunk(float posX, float posY)
	{
		Vector2ic chunkPos = Chunk.toChunkPos(posX, posY);
		Chunk chunk = this.activeChunks.get(chunkPos);
		if (chunk == null)
		{
			chunk = this.chunkLoader.load(chunkPos);
			this.activeChunks.put(chunk.getChunkPos(), chunk);
		}
		return chunk;
	}

	public Collection<Chunk> getActiveChunks()
	{
		return this.activeChunks.values();
	}

	public long getCurrentTime()
	{
		return System.currentTimeMillis();
	}

	public ChunkLoader getChunkLoader()
	{
		return this.chunkLoader;
	}
}
