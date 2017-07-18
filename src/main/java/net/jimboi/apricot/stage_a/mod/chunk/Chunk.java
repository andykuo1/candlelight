package net.jimboi.apricot.stage_a.mod.chunk;

import org.joml.Vector2i;
import org.joml.Vector2ic;

/**
 * Created by Andy on 5/13/17.
 */
public class Chunk
{
	public static final int SIZE = 16;

	public static Vector2i toChunkPos(float posX, float posY)
	{
		return new Vector2i(
				((int)Math.floor(posX)) / Chunk.SIZE,
				((int)Math.floor(posY)) / Chunk.SIZE);
	}

	public static int toChunkLayerIndex(float posZ)
	{
		int z = (int)Math.floor(posZ);
		return (z % SIZE) / 2;
	}

	public static int toChunkTileIndex(float posX, float posY)
	{
		int tileX = ((int)Math.floor(posX)) % Chunk.SIZE;
		int tileY = ((int)Math.floor(posY)) % Chunk.SIZE;
		return tileX + tileY * Chunk.SIZE;
	}

	private final ChunkData chunkData;
	private final Vector2ic chunkPos;

	private long activeTime = -1;
	private boolean dirty = true;

	public Chunk(ChunkData chunkData, int chunkX, int chunkY)
	{
		this.chunkData = chunkData;
		this.chunkPos = new Vector2i(chunkX, chunkY);

		this.dirty = true;
	}

	public void onChunkUpdate(ChunkManager chunkManager)
	{
		if (this.dirty)
		{
			this.activeTime = chunkManager.getCurrentTime();
			this.dirty = false;
		}
	}

	public long getLastActiveTime()
	{
		return this.activeTime;
	}

	public ChunkData getChunkData()
	{
		this.dirty = true;
		return this.chunkData;
	}

	public Vector2ic getChunkPos()
	{
		return this.chunkPos;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Chunk)
		{
			return this.chunkPos.equals(((Chunk) o).chunkPos);
		}

		return false;
	}
}
