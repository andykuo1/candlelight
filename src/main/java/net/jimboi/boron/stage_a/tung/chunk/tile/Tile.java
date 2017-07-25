package net.jimboi.boron.stage_a.tung.chunk.tile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Andy on 5/1/17.
 */
public abstract class Tile
{
	private static final Map<String, Tile> tiles = new HashMap<>();

	public static Tile register(int tileID, String name, Tile tile)
	{
		tiles.put(name, tile);
		tile.setTileID(tileID);
		return tile;
	}

	public static final Tile dirt = register(0, "dirt", new TileBase());
	public static final Tile stone = register(1, "stone", new TileBase());

	public static Tile getTileByName(String name)
	{
		return tiles.get(name);
	}

	public static Tile getTileByID(int tileID)
	{
		Iterator<Tile> iter = tiles.values().iterator();
		while(iter.hasNext())
		{
			Tile tile = iter.next();
			if (tile.getTileID() == tileID)
			{
				return tile;
			}
		}
		return null;
	}

	private String tileName;
	private int tileID = -1;

	public Tile()
	{

	}

	private void setTileID(int tileID)
	{
		this.tileID = tileID;
	}

	public int getTileID()
	{
		return this.tileID;
	}

	private void setTileName(String tileName)
	{
		this.tileName = tileName;
	}

	public String getTileName()
	{
		return this.tileName;
	}
}
