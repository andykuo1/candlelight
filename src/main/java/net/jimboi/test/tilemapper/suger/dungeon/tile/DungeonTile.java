package net.jimboi.test.tilemapper.suger.dungeon.tile;

import net.jimboi.test.tilemapper.suger.dungeon.DungeonTileRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Created by Andy on 11/15/17.
 */
public abstract class DungeonTile
{
	private static final Map<Integer, DungeonTile> TILES = new HashMap<>();

	public static DungeonTile register(int id, DungeonTile tile)
	{
		tile.id = id;
		TILES.put(id, tile);
		return tile;
	}

	public static DungeonTile getTileByID(int id)
	{
		return TILES.get(id);
	}

	public static int getNumOfRegisteredTiles()
	{
		return TILES.size();
	}

	protected static final Random RAND = new Random();

	int id;

	public abstract void draw(DungeonTileRenderer renderer, GraphicsContext g);

	public abstract boolean isSolid();

	public boolean isPermeable()
	{
		return false;
	}

	public final int getTileID()
	{
		return this.id;
	}

	protected static void drawConnectedBlock(DungeonTileRenderer renderer, GraphicsContext g, Paint p, double border)
	{
		if (p == null) p = getRegionalPaint(renderer.region);

		g.setFill(p);
		g.fillRect(0, 0, 1, 1);

		byte direction = renderer.direction;

		if (border > 0 && direction != 15)
		{
			boolean east = (direction & 1) != 0;
			boolean north = (direction & 2) != 0;
			boolean west = (direction & 4) != 0;
			boolean south = (direction & 8) != 0;

			if (!east) g.clearRect(1 - border, 0, border, 1);
			if (!north) g.clearRect(0, 0, 1, border);
			if (!west) g.clearRect(0, 0, border, 1);
			if (!south) g.clearRect(0, 1 - border, 1, border);
		}
	}

	protected static void drawBlock(DungeonTileRenderer renderer, GraphicsContext g, Paint p, double offsetX, double offsetY, double width, double height)
	{
		if (p == null) p = getRegionalPaint(renderer.region);

		g.setFill(p);
		g.fillRect(offsetX, offsetY, width, height);
	}

	protected static Paint getRegionalPaint(int region)
	{
		RAND.setSeed(region);
		return Color.hsb(RAND.nextInt(360), 0.5, 0.7);
	}
}
