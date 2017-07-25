package net.jimboi.boron.stage_a.tung.chunk;

import org.joml.Vector2ic;
import org.qsilver.util.map2d.ByteMap;
import org.qsilver.util.map2d.IntMap;

/**
 * Created by Andy on 5/13/17.
 */
public class ChunkLoader
{
	public Chunk load(Vector2ic chunkPos)
	{
		IntMap tilemap = new IntMap(16, 16);
		ByteMap tiledata = new ByteMap(16, 16);
		for(int i = 0; i < tilemap.size(); ++i)
		{
			tilemap.set(i, (int)(Math.random() * 3));
		}

		ChunkData chunkData = new ChunkData(tilemap, tiledata);

		return new Chunk(chunkData, chunkPos.x(), chunkPos.y());
	}
}
