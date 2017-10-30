package net.jimboi.canary.stage_a.cuplet.scene_main.tile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 9/1/17.
 */
public abstract class Tile
{
	private static final Map<Integer, Tile> TILES = new HashMap<>();

	public static Tile register(int id, Tile tile)
	{
		tile.id = id;
		TILES.put(tile.getID(), tile);
		return tile;
	}

	public static Tile getTileByID(int id)
	{
		return TILES.get(id);
	}

	public static Iterable<Tile> getTiles()
	{
		return TILES.values();
	}

	private int id;

	public float getFriction()
	{
		return 1;
	}

	public boolean isSolid()
	{
		return false;
	}

	public final int getID()
	{
		return this.id;
	}

	@Override
	public final boolean equals(Object o)
	{
		if (o instanceof Tile)
		{
			return ((Tile) o).id == this.id;
		}

		return false;
	}
}
