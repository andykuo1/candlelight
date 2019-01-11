package canary.test.tilemapper.suger.dungeon.tile;

import canary.test.tilemapper.suger.dungeon.DungeonTileRenderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Andy on 11/15/17.
 */
public class TileItem extends DungeonTile
{
	@Override
	public void draw(DungeonTileRenderer renderer, GraphicsContext g)
	{
		drawConnectedBlock(renderer, g, Color.AQUA, 0.4);
	}

	public boolean shouldRender3D()
	{
		return true;
	}

	@Override
	public boolean isSolid()
	{
		return false;
	}
}
