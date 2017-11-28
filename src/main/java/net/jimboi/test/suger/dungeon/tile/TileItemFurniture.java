package net.jimboi.test.suger.dungeon.tile;

import net.jimboi.test.suger.dungeon.DungeonTileRenderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by Andy on 11/22/17.
 */
public class TileItemFurniture extends TileItem
{
	private static final double OUTLINE_WIDTH = 0.05;

	protected final double size;
	protected Paint mainPaint;
	protected Paint outlinePaint;

	public TileItemFurniture(double size)
	{
		this.size = size;
	}

	public TileItemFurniture setMainPaint(Paint paint)
	{
		this.mainPaint = paint;
		return this;
	}

	public TileItemFurniture setOutlinePaint(Paint paint)
	{
		this.outlinePaint = paint;
		return this;
	}

	@Override
	public void draw(DungeonTileRenderer renderer, GraphicsContext g)
	{
		double dx = (1 - this.size) * 0.5;
		double dy = (1 - this.size) * 0.5;
		drawBlock(renderer, g, this.mainPaint, dx, dy, 1 - dx - dx, 1 - dy - dy);

		if (this.outlinePaint != null)
		{
			g.setStroke(this.outlinePaint);
		}

		g.setLineWidth(OUTLINE_WIDTH);
		g.strokeRect(dx, dy, 1 - dx - dx, 1 - dy - dy);
	}
}
