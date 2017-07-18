package net.jimboi.apricot.stage_a.mod.chunk;

/**
 * Created by Andy on 5/13/17.
 */
public class ChunkMap
{
	private final int[] blockmap;
	private final byte[] blockdata;

	private final int[] tilemap;
	private final byte[] tiledata;

	ChunkMap(int[] blockmap, byte[] blockdata,
	         int[] tilemap, byte[] tiledata)
	{
		this.blockmap = blockmap;
		this.blockdata = blockdata;
		this.tilemap = tilemap;
		this.tiledata = tiledata;
	}

	public int getBlockID(int index)
	{
		return this.blockmap[index];
	}

	public byte getBlockData(int index)
	{
		return this.blockdata[index];
	}

	public int getTileID(int index)
	{
		return this.tilemap[index];
	}

	public byte getTileData(int index)
	{
		return this.tiledata[index];
	}
}
