package canary.test.tilemapper.suger.dungeon.tile;

import canary.test.tilemapper.suger.dungeon.DungeonTileRenderer;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Andy on 11/15/17.
 */
public class TileAir extends DungeonTile
{
	@Override
	public final void draw(DungeonTileRenderer renderer, GraphicsContext g)
	{
	}

	@Override
	public boolean isSolid()
	{
		return false;
	}
}
