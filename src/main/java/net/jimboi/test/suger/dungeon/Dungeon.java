package net.jimboi.test.suger.dungeon;

import net.jimboi.test.suger.dungeon.tile.DungeonTile;
import net.jimboi.test.suger.dungeon.tile.DungeonTiles;

import org.bstone.util.chunk.Chunk;
import org.bstone.util.chunk.ChunkLoader;
import org.bstone.util.chunk.ChunkMap;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 11/15/17.
 */
public class Dungeon implements ChunkLoader<DungeonChunk>
{
	private final ChunkMap<DungeonChunk> chunks;
	private final Map<Integer, DungeonTile> regionTiles;

	public Dungeon()
	{
		this.chunks = new ChunkMap<>(this, DungeonChunk.CHUNK_SIZE);
		this.regionTiles = new HashMap<>();
	}

	@Override
	public DungeonChunk onChunkLoad(ChunkMap<DungeonChunk> chunkMap,
	                                Vector2i chunkCoord, int chunkSize)
	{
		return new DungeonChunk(chunkCoord.x(), chunkCoord.y(), chunkSize);
	}

	@Override
	public void onChunkUnload(ChunkMap<DungeonChunk> chunkMap,
	                          Vector2ic chunkCoord, DungeonChunk chunk)
	{

	}

	@Override
	public void onChunkUpdate(ChunkMap<DungeonChunk> chunkMap,
	                          DungeonChunk chunk)
	{

	}

	private final Vector2i vec2i = new Vector2i();

	public boolean isPermeable(float posX, float posY)
	{
		final DungeonChunk chunk = this.chunks.getChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i), false);
		if (chunk != null)
		{
			return chunk.permeables.get(Chunk.getTileCoord(posX, posY, this.vec2i));
		}
		return false;
	}

	public boolean isSolid(float posX, float posY)
	{
		final DungeonChunk chunk = this.chunks.getChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i), false);
		if (chunk != null)
		{
			return chunk.solids.get(Chunk.getTileCoord(posX, posY, this.vec2i));
		}
		return false;
	}

	public boolean setSolid(float posX, float posY, boolean value)
	{
		final DungeonChunk chunk = this.chunks.loadChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i));
		boolean result = chunk.solids.set(Chunk.getTileCoord(posX, posY, this.vec2i), value);
		chunk.permeables.set(this.vec2i, false);
		return result;
	}

	public int getRegion(float posX, float posY)
	{
		final DungeonChunk chunk = this.chunks.getChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i), false);
		if (chunk != null)
		{
			return chunk.regions.get(Chunk.getTileCoord(posX, posY, this.vec2i));
		}
		return 0;
	}

	public int setRegion(float posX, float posY, int value)
	{
		final DungeonChunk chunk = this.chunks.loadChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i));
		return chunk.regions.set(Chunk.getTileCoord(posX, posY, this.vec2i), value);
	}

	public byte getDirection(float posX, float posY)
	{
		final DungeonChunk chunk = this.chunks.getChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i), false);
		if (chunk != null)
		{
			return chunk.directions.get(Chunk.getTileCoord(posX, posY, this.vec2i));
		}
		return 0;
	}

	public byte setDirection(float posX, float posY, byte value)
	{
		final DungeonChunk chunk = this.chunks.loadChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i));
		return chunk.directions.set(Chunk.getTileCoord(posX, posY, this.vec2i), value);
	}

	public void addDirection(float posX, float posY, byte value)
	{
		final DungeonChunk chunk = this.chunks.loadChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i));
		Vector2ic tileCoord = Chunk.getTileCoord(posX, posY, this.vec2i);
		chunk.directions.add(tileCoord.x(), tileCoord.y(), value);
	}

	public DungeonTile getItem(float posX, float posY)
	{
		final DungeonChunk chunk = this.chunks.getChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i), false);
		if (chunk != null)
		{
			return DungeonTile.getTileByID(chunk.items.get(Chunk.getTileCoord(posX, posY, this.vec2i)));
		}
		return DungeonTiles.AIR;
	}

	public int setItem(float posX, float posY, DungeonTile value)
	{
		final DungeonChunk chunk = this.chunks.loadChunk(ChunkMap.getChunkCoord(posX, posY, this.vec2i));
		int result = chunk.items.set(Chunk.getTileCoord(posX, posY, this.vec2i), value.getTileID());
		chunk.solids.set(this.vec2i, value.isSolid());
		chunk.permeables.set(this.vec2i, value.isPermeable());
		return result;
	}

	public DungeonTile getBlockTile(int region)
	{
		if (region == -1) return DungeonTiles.AIR;
		DungeonTile tile = this.regionTiles.get(region);
		if (tile == null) tile = DungeonTiles.PLACEHOLDER;
		return tile;
	}

	public void setBlockTile(int region, DungeonTile tile)
	{
		this.regionTiles.put(region, tile);
	}

	public boolean isChunkLoaded(float posX, float posY)
	{
		return this.chunks.isChunkLoaded(ChunkMap.getChunkCoord(posX, posY, this.vec2i));
	}

	public final ChunkMap<DungeonChunk> getMap()
	{
		return this.chunks;
	}
}
