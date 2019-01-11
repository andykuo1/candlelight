package canary.test.tilemapper.suger.dungeon.tile;

import canary.test.tilemapper.suger.dungeon.DungeonTileRenderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Paint;

/**
 * Created by Andy on 11/15/17.
 */
public class TileItemStairs extends TileItem
{
	private static final double SIZE_STEP = 0.3;

	protected Paint stairPaint;

	public TileItemStairs setStairPaint(Paint paint)
	{
		this.stairPaint = paint;
		return this;
	}

	@Override
	public void draw(DungeonTileRenderer renderer, GraphicsContext g)
	{
		g.setFill(this.stairPaint);
		g.fillRect(0, 0, 1, 1);

		g.setFill(this.stairPaint);
		g.setEffect(new ColorAdjust(0, 0, -0.3, 0));
		g.fillRect(0, SIZE_STEP, 1, SIZE_STEP);
	}

	@Override
	public boolean shouldRender3D()
	{
		return true;
	}
}
