package net.jimboi.test.suger.dungeon;

import net.jimboi.test.suger.dungeon.tile.DungeonTile;
import net.jimboi.test.suger.dungeon.tile.DungeonTiles;

import org.bstone.json.JSON;
import org.bstone.json.JSONArray;
import org.bstone.json.JSONBoolean;
import org.bstone.json.JSONNumber;
import org.bstone.json.JSONObject;
import org.bstone.json.JSONValue;
import org.bstone.util.chunk.Chunk;
import org.bstone.util.chunk.ChunkLoader;
import org.bstone.util.chunk.ChunkMap;
import org.bstone.util.grid.BooleanMap;
import org.bstone.util.grid.ByteMap;
import org.bstone.util.grid.IntMap;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 11/15/17.
 */
public class Dungeon implements ChunkLoader<DungeonChunk>
{
	private static final String CHUNKS = "chunks";
	private static final String CHUNK_SIZE = "size";
	private static final String COORDX = "coordX";
	private static final String COORDY = "coordY";
	private static final String MAP_PERMEABLES = "permeables";
	private static final String MAP_SOLIDS = "solids";
	private static final String MAP_DIRECTIONS = "directions";
	private static final String MAP_REGIONS = "regions";
	private static final String MAP_ITEMS = "items";

	public static Dungeon loadFromJSON(JSONObject data)
	{
		Dungeon dungeon = new Dungeon();
		JSONArray chunks = (JSONArray) data.get(CHUNKS);
		for(JSONValue c : chunks)
		{
			JSONObject obj = (JSONObject) c;
			JSONNumber coordX = (JSONNumber) obj.get(COORDX);
			JSONNumber coordY = (JSONNumber) obj.get(COORDY);
			JSONNumber size = (JSONNumber) obj.get(CHUNK_SIZE);

			int chunkSize = size.toInt();
			BooleanMap solids = new BooleanMap(chunkSize, chunkSize);
			BooleanMap permeables = new BooleanMap(chunkSize, chunkSize);
			ByteMap directions = new ByteMap(chunkSize, chunkSize);
			IntMap regions = new IntMap(chunkSize, chunkSize);
			IntMap items = new IntMap(chunkSize, chunkSize);

			JSONArray array;

			array = (JSONArray) obj.get(MAP_SOLIDS);
			for(int i = 0; i < solids.array().length; ++i)
			{
				solids.array()[i] = ((JSONBoolean) array.get(i)).toBoolean();
			}

			array = (JSONArray) obj.get(MAP_PERMEABLES);
			for(int i = 0; i < permeables.array().length; ++i)
			{
				permeables.array()[i] = ((JSONBoolean) array.get(i)).toBoolean();
			}

			array = (JSONArray) obj.get(MAP_DIRECTIONS);
			for(int i = 0; i < directions.array().length; ++i)
			{
				directions.array()[i] = (byte) ((JSONNumber) array.get(i)).toInt();
			}

			array = (JSONArray) obj.get(MAP_REGIONS);
			for(int i = 0; i < directions.array().length; ++i)
			{
				regions.array()[i] = ((JSONNumber) array.get(i)).toInt();
			}

			array = (JSONArray) obj.get(MAP_ITEMS);
			for(int i = 0; i < directions.array().length; ++i)
			{
				items.array()[i] = ((JSONNumber) array.get(i)).toInt();
			}

			DungeonChunk chunk = new DungeonChunk(coordX.toInt(), coordY.toInt(), chunkSize,
					solids, permeables, directions, regions, items);
			dungeon.getMap().cacheChunk(chunk);
		}

		return dungeon;

	}

	public static JSONObject writeToJSON(Dungeon dungeon)
	{
		JSONObject root = JSON.object();
		JSONArray chunks = JSON.array();
		for(DungeonChunk chunk : dungeon.getMap().getLoadedChunks())
		{
			JSONObject c = JSON.object();
			c.put(COORDX, JSON.value(chunk.getChunkCoordX()));
			c.put(COORDY, JSON.value(chunk.getChunkCoordY()));
			c.put(CHUNK_SIZE, JSON.value(chunk.getChunkSize()));

			JSONArray solids = JSON.array();
			for(boolean b : chunk.solids.array()) solids.add(JSON.value(b));
			c.put(MAP_SOLIDS, solids);

			JSONArray permeables = JSON.array();
			for(boolean b : chunk.permeables.array()) permeables.add(JSON.value(b));
			c.put(MAP_PERMEABLES, permeables);

			JSONArray directions = JSON.array();
			for(byte b : chunk.directions.array()) directions.add(JSON.value(b));
			c.put(MAP_DIRECTIONS, directions);

			JSONArray regions = JSON.array();
			for(int i : chunk.regions.array()) regions.add(JSON.value(i));
			c.put(MAP_REGIONS, regions);

			JSONArray items = JSON.array();
			for(int i : chunk.items.array()) items.add(JSON.value(i));
			c.put(MAP_ITEMS, items);

			chunks.add(c);
		}
		root.put(CHUNKS, chunks);
		return root;
	}

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
