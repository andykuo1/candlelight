package net.jimboi.boron.stage_a.smack.chunk;

import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;

import org.joml.Vector2i;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 8/6/17.
 */
public class ChunkManager
{
	private Map<Vector2i, Chunk> chunks = new HashMap<>();

	public ChunkManager()
	{

	}

	public void update(SmackWorld world)
	{
		Set<Vector2i> destroyCache = new HashSet<>();
		long currentTime = System.currentTimeMillis();
		for(Chunk chunk : this.chunks.values())
		{
			if (currentTime - chunk.getLastActiveTime() > 5000)
			{
				destroyCache.add(new Vector2i(chunk.getChunkX(), chunk.getChunkY()));
			}
		}

		for(Vector2i vec : destroyCache)
		{
			Chunk chunk = this.chunks.remove(vec);
			this.unloadChunk(world, chunk);
		}
	}

	public Chunk loadChunk(SmackWorld world, float posX, float posY)
	{
		int chunkX = (int) Math.floor(posX / Chunk.CHUNK_SIZE);
		int chunkY = (int) Math.floor(posY / Chunk.CHUNK_SIZE);
		Vector2i chunkPos = new Vector2i(chunkX, chunkY);
		Chunk chunk = this.chunks.get(chunkPos);
		if (chunk == null)
		{
			chunk = new Chunk(world, chunkX, chunkY, chunkX + chunkY << 7);
			for(SmackEntity entity : chunk.getEntities())
			{
				world.spawn(entity);
			}
			this.chunks.put(chunkPos, chunk);
			System.out.println("LOADED CHUNK: " + chunkPos);
		}
		chunk.markDirty();
		return chunk;
	}

	public void unloadChunk(SmackWorld world, Chunk chunk)
	{
		for(SmackEntity entity : chunk.getEntities())
		{
			if (!entity.isDead())
			{
				entity.setDead();
			}
		}
		chunk.getEntities().clear();
		System.out.println("UNLOADED CHUNK: " + new Vector2i(chunk.getChunkX(), chunk.getChunkY()));
	}
}
