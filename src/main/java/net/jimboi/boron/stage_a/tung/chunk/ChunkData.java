package net.jimboi.boron.stage_a.tung.chunk;

import org.qsilver.util.map2d.ByteMap;
import org.qsilver.util.map2d.IntMap;

/**
 * Created by Andy on 5/13/17.
 */
public class ChunkData
{
	private final IntMap tilemap;
	private final ByteMap tiledata;

	ChunkData(IntMap tilemap, ByteMap tiledata)
	{
		this.tilemap = tilemap;
		this.tiledata = tiledata;
	}

	public IntMap getTileMap()
	{
		return this.tilemap;
	}

	public ByteMap getTileData()
	{
		return this.tiledata;
	}
}
