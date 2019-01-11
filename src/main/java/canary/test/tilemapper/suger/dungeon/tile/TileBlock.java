package canary.test.tilemapper.suger.dungeon.tile;

import canary.test.tilemapper.suger.dungeon.DungeonTileRenderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by Andy on 11/15/17.
 */
public class TileBlock extends DungeonTile
{
	protected Paint blockPaint;

	public TileBlock setBlockPaint(Paint paint)
	{
		this.blockPaint = paint;
		return this;
	}

	@Override
	public void draw(DungeonTileRenderer renderer, GraphicsContext g)
	{
		drawConnectedBlock(renderer, g, this.blockPaint, 0);
	}

	@Override
	public boolean isSolid()
	{
		return true;
	}
}
