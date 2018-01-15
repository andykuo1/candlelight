package net.jimboi.test.tilemapper.suger.dungeon.tile;

import net.jimboi.test.tilemapper.suger.dungeon.DungeonTileRenderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by Andy on 11/15/17.
 */
public class TileItemDoor extends TileItem
{
	protected Paint doorPaint;

	public TileItemDoor setDoorPaint(Paint paint)
	{
		this.doorPaint = paint;
		return this;
	}

	@Override
	public void draw(DungeonTileRenderer renderer, GraphicsContext g)
	{
		if (renderer.direction < 0)
		{
			renderer.direction = 15;
		}
		else
		{
			int direction = renderer.direction;
			boolean east = (direction & 1) != 0;
			boolean north = (direction & 2) != 0;
			boolean west = (direction & 4) != 0;
			boolean south = (direction & 8) != 0;

			if (east || west) renderer.direction = 10;
			else if (north || south) renderer.direction = 5;
			else renderer.direction = 0;
		}

		drawConnectedBlock(renderer, g, this.doorPaint, 0.25);
	}

	@Override
	public final boolean isPermeable()
	{
		return true;
	}
}
