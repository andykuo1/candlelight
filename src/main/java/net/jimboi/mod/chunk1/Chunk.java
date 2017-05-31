package net.jimboi.mod.chunk1;

import java.util.Random;

/**
 * Created by Andy on 5/28/17.
 */
public class Chunk
{
	public static final int CHUNK_SIZE = 16;
	public static final float TILE_SIZE = 1;
	public static final Random RAND = new Random();

	private final int[] heightMap;

	public Chunk()
	{
		this.heightMap = new int[CHUNK_SIZE];
	}

	public void generate(long seed)
	{
		RAND.setSeed(seed);
		float y = 0;

		this.heightMap[0] = 0;
		for (int i = 1; i < this.heightMap.length - 1; ++i)
		{
			y += (RAND.nextFloat() * 5);
			this.heightMap[i] = (int) y;
		}

		this.heightMap[this.heightMap.length - 1] = 0;
	}

	public boolean isValidTilePos(float tilePos)
	{
		return tilePos >= 0 && tilePos < CHUNK_SIZE;
	}

	public float getTilePos(float posX)
	{
		return (posX / TILE_SIZE) % CHUNK_SIZE;
	}

	public float getHeightByTilePos(float tilePos)
	{
		int prevIndex = (int) Math.floor(tilePos);
		int nextIndex = (int) Math.ceil(tilePos);
		if (prevIndex == nextIndex) return this.heightMap[prevIndex];

		int prevHeight = this.heightMap[prevIndex];
		int nextHeight = this.heightMap[nextIndex];
		int slope = nextHeight - prevHeight;
		float slopePos = (tilePos % TILE_SIZE) * slope;
		return prevHeight + slopePos;
	}

	public float getHeightByPos(float posX)
	{
		float tilePos = this.getTilePos(posX);
		return this.getHeightByTilePos(tilePos);
	}
}
