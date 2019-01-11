package canary.bstone.util.chunk;

import canary.test.tilemapper.suger.dungeon.DungeonChunk;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Andy on 11/13/17.
 */
public class ChunkMap<T extends Chunk>
{
	private final Map<Vector2ic, T> chunks = new HashMap<>();
	private final ChunkLoader<T> loader;
	private final int chunkSize;

	private final Queue<T> updateQueue = new LinkedList<>();

	public ChunkMap(ChunkLoader<T> loader, int chunkSize)
	{
		this.loader = loader;
		this.chunkSize = chunkSize;
	}

	public void update()
	{
		while(!this.updateQueue.isEmpty())
		{
			this.loader.onChunkUpdate(this, this.updateQueue.poll());
		}
	}

	public T getChunk(Vector2ic chunkCoord)
	{
		return this.chunks.get(chunkCoord);
	}

	public T getChunk(Vector2ic chunkCoord, boolean markForUpdate)
	{
		T chunk = this.getChunk(chunkCoord);
		if (markForUpdate && chunk != null)
		{
			this.markChunkForUpdate(chunk);
		}
		return chunk;
	}

	public void cacheChunk(T chunk)
	{
		this.chunks.put(new Vector2i(chunk.chunkX, chunk.chunkY), chunk);
	}

	public T loadChunk(Vector2ic chunkCoord)
	{
		T chunk = this.chunks.get(chunkCoord);
		if (chunk == null)
		{
			Vector2i key = new Vector2i(chunkCoord);
			chunk = this.loader.onChunkLoad(this, key, this.chunkSize);
			this.chunks.put(key, chunk);
		}
		else
		{
			this.markChunkForUpdate(chunk);
		}
		return chunk;
	}

	public T unloadChunk(Vector2ic chunkCoord)
	{
		T chunk = this.chunks.remove(chunkCoord);
		if (chunk != null)
		{
			this.loader.onChunkUnload(this, chunkCoord, chunk);
			this.updateQueue.remove(chunk);
		}
		return chunk;
	}

	public void updateChunk(T chunk)
	{
		this.loader.onChunkUpdate(this, chunk);
		this.updateQueue.remove(chunk);
	}

	public boolean isChunkLoaded(Vector2ic chunkCoord)
	{
		return this.chunks.containsKey(chunkCoord);
	}

	public void markChunkForUpdate(T chunk)
	{
		this.updateQueue.add(chunk);
	}

	public Iterable<T> getLoadedChunks()
	{
		return this.chunks.values();
	}

	public Iterable<T> getChunksWaitingForUpdate()
	{
		return this.updateQueue;
	}

	public ChunkLoader<T> getChunkLoader()
	{
		return this.loader;
	}

	public int getChunkSize()
	{
		return this.chunkSize;
	}

	public static Vector2i getChunkCoord(float posX, float posY, Vector2i dst)
	{
		float x = posX / DungeonChunk.CHUNK_SIZE;
		float y = posY / DungeonChunk.CHUNK_SIZE;

		return dst.set((int) Math.floor(x), (int) Math.floor(y));
	}
}
