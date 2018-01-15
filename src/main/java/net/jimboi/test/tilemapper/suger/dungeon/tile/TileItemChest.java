package net.jimboi.test.tilemapper.suger.dungeon.tile;

import net.jimboi.test.tilemapper.suger.dungeon.DungeonTileRenderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by Andy on 11/15/17.
 */
public class TileItemChest extends TileItem
{
	private static final double MAX_BORDER = 0.3;
	private static final double MIN_BORDER = 0.1;

	private static final double OUTLINE_WIDTH = 0.05;

	protected Paint chestPaint;
	protected Paint outlinePaint;

	public TileItemChest setChestPaint(Paint paint)
	{
		this.chestPaint = paint;
		return this;
	}

	public TileItemChest setOutlinePaint(Paint paint)
	{
		this.outlinePaint = paint;
		return this;
	}

	@Override
	public void draw(DungeonTileRenderer renderer, GraphicsContext g)
	{
		RAND.setSeed((long)(renderer.posX * 17 + renderer.posY));
		double ds = MAX_BORDER - MIN_BORDER;
		double dx = RAND.nextDouble() * ds + MIN_BORDER;
		double dy = RAND.nextDouble() * ds + MIN_BORDER;
		double dw = RAND.nextDouble() * ds + MIN_BORDER;
		double dh = RAND.nextDouble() * ds + MIN_BORDER;
		drawBlock(renderer, g, this.chestPaint, dx, dy, 1 - dx - dw, 1 - dy - dh);

		if (this.outlinePaint != null)
		{
			g.setStroke(this.outlinePaint);
		}

		g.setLineWidth(OUTLINE_WIDTH);
		g.strokeRect(dx, dy, 1 - dx - dw, 1 - dy - dh);
	}
}
