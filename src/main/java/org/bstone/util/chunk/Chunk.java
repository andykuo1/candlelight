package org.bstone.util.chunk;

import net.jimboi.test.tilemapper.suger.dungeon.DungeonChunk;

import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Created by Andy on 11/13/17.
 */
public abstract class Chunk
{
	protected final int chunkX;
	protected final int chunkY;
	protected final int chunkSize;

	public Chunk(int chunkCoordX, int chunkCoordY, int chunkSize)
	{
		this.chunkX = chunkCoordX;
		this.chunkY = chunkCoordY;
		this.chunkSize = chunkSize;
	}

	public final Vector2f getChunkPos(Vector2f dst)
	{
		return dst.set(this.chunkX * this.chunkSize,
				this.chunkY * this.chunkSize);
	}

	public final Vector2i getChunkCoord(Vector2i dst)
	{
		return dst.set(this.chunkX, this.chunkY);
	}

	public final int getChunkCoordX()
	{
		return this.chunkX;
	}

	public final int getChunkCoordY()
	{
		return this.chunkY;
	}

	public final int getChunkSize()
	{
		return this.chunkSize;
	}

	@Override
	public final boolean equals(Object o)
	{
		return o instanceof Chunk &&
				this.chunkX == ((Chunk) o).chunkX &&
				this.chunkY == ((Chunk) o).chunkY;
	}

	public static Vector2i getTileCoord(float posX, float posY, Vector2i dst)
	{
		float x = posX % DungeonChunk.CHUNK_SIZE;
		float y = posY % DungeonChunk.CHUNK_SIZE;

		if (x < 0) x += DungeonChunk.CHUNK_SIZE;
		if (y < 0) y += DungeonChunk.CHUNK_SIZE;

		return dst.set((int) Math.floor(x), (int) Math.floor(y));
	}
}
