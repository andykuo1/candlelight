package canary.test.tilemapper.suger;

import canary.test.tilemapper.suger.baron.ViewPort;
import canary.test.tilemapper.suger.canvas.LayeredCanvas;
import canary.test.tilemapper.suger.dungeon.Dungeon;
import canary.test.tilemapper.suger.dungeon.DungeonChunk;
import canary.test.tilemapper.suger.dungeon.DungeonTileRenderer;
import canary.test.tilemapper.suger.dungeon.tile.DungeonTile;
import canary.test.tilemapper.suger.dungeon.tile.TileItem;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by Andy on 11/13/17.
 */
public class Renderer
{
	public static final int LAYERS = 5;
	public static final int LAYER_FLOOR = 0;
	public static final int LAYER_SHADOW = 1;
	public static final int LAYER_ITEM = 2;
	public static final int LAYER_SOLID = 3;
	public static final int LAYER_GUI = 4;

	private static final int GRID_LINE_WIDTH = 1;
	private static final int AXIS_LINE_WIDTH = 4;
	private static final int SLOT_LINE_WIDTH = 4;
	private static final int EXPORT_LINE_WIDTH = 6;

	private static final double SHADOW_SIZE = 0.4;
	private static final double SHADOW_BLUR = 1.5;
	private static final double SHADOW_BRIGHTNESS = -0.32;

	private static final Color COLOR_XAXIS = Color.BLUE;
	private static final Color COLOR_YAXIS = Color.RED;
	private static final Color COLOR_GRID = Color.DARKGRAY;
	private static final Color COLOR_CURSOR = Color.WHITE.deriveColor(0, 1, 1, 0.2);

	private static final Color COLOR_EXPORT_AREA = Color.RED;

	public static void drawExportArea(GraphicsContext g, Rectangle2D area)
	{
		g.setStroke(COLOR_EXPORT_AREA);
		g.setLineWidth(EXPORT_LINE_WIDTH);
		g.strokeRect(area.getMinX(), area.getMinY(),
				area.getWidth(), area.getHeight());
	}

	public static void drawCursor(GraphicsContext g, ViewPort v, Vector2fc mouse)
	{
		Vector2dc offset = ViewPort.getScreenPos(v,
				(int) Math.floor(mouse.x()),
				(int) Math.floor(mouse.y()),
				new Vector2d());

		g.save();
		g.translate(offset.x(), offset.y());
		g.scale(v.getUnitScale(), v.getUnitScale());
		g.setFill(COLOR_CURSOR);
		g.fillRect(0, 0, 1, 1);
		g.restore();
	}

	public static void drawDungeon(LayeredCanvas canvas, ViewPort v, Dungeon dungeon, boolean draft)
	{
		GraphicsContext solids = canvas.getCanvas(LAYER_SOLID).getGraphicsContext2D();
		GraphicsContext shadows = canvas.getCanvas(LAYER_SHADOW).getGraphicsContext2D();
		GraphicsContext items = canvas.getCanvas(LAYER_ITEM).getGraphicsContext2D();
		GraphicsContext floors = canvas.getCanvas(LAYER_FLOOR).getGraphicsContext2D();

		DungeonTileRenderer renderer = new DungeonTileRenderer();
		Vector2f vec2f = new Vector2f();
		Vector2d vec2d = new Vector2d();
		for(DungeonChunk chunk : dungeon.getMap().getLoadedChunks())
		{
			Vector2fc chunkPos = chunk.getChunkPos(vec2f);

			for(int i = 0; i < chunk.getChunkSize(); ++i)
			{
				for(int j = 0; j < chunk.getChunkSize(); ++j)
				{
					Vector2dc offset = ViewPort.getScreenPos(v,
							(int) Math.floor(chunkPos.x() + i),
							(int) Math.floor(chunkPos.y() + j),
							vec2d);

					renderer.posX = chunkPos.x() + i;
					renderer.posY = chunkPos.y() + j;
					renderer.solid = chunk.solids.get(i, j);
					renderer.permeable = chunk.permeables.get(i, j);
					renderer.direction = chunk.directions.get(i, j);
					renderer.region = chunk.regions.get(i, j);
					renderer.item = chunk.items.get(i, j);

					//Background Pass
					drawTile(renderer.solid ? solids : floors,
							renderer, dungeon.getBlockTile(renderer.region),
							offset.x(), offset.y(),
							v.getUnitScale(), v.getUnitScale());

					//Foreground Pass
					if (renderer.item != 0)
					{
						GraphicsContext g;
						DungeonTile tile = DungeonTile.getTileByID(renderer.item);
						if (tile instanceof TileItem)
						{
							g = ((TileItem) tile).shouldRender3D() ? solids : items;
						}
						else
						{
							g = floors;
						}

						drawTile(g,
								renderer, tile,
								offset.x(), offset.y(),
								v.getUnitScale(), v.getUnitScale());
					}
				}
			}
		}

		if (!draft)
		{
			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			WritableImage image = canvas.getCanvas(LAYER_SOLID).snapshot(params, null);
			drawShadow(image, v.getUnitScale(), shadows);
		}
	}

	public static void drawShadow(Image image, double scale, GraphicsContext dst)
	{
		final double shadowSize = scale * SHADOW_SIZE;
		final double shadowBlur = scale * SHADOW_BLUR;

		GaussianBlur blur = new GaussianBlur(shadowBlur);
		dst.drawImage(image, shadowSize * 2 / 3, shadowSize);
		dst.applyEffect(blur);

		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(SHADOW_BRIGHTNESS);
		dst.applyEffect(colorAdjust);

		dst.drawImage(image, 0, shadowSize);
		dst.applyEffect(colorAdjust);
	}

	public static void drawTile(GraphicsContext g, DungeonTileRenderer renderer, DungeonTile tile, double x, double y, double width, double height)
	{
		renderer.tile = tile;

		g.save();
		g.translate(x, y);
		g.scale(width, height);
		renderer.tile.draw(renderer, g);
		g.restore();
	}

	public static void drawGrid(GraphicsContext g, ViewPort v)
	{
		double dx = v.getX() % v.getUnitScale();
		double dy = v.getY() % v.getUnitScale();

		g.setStroke(COLOR_GRID);
		g.setLineWidth(GRID_LINE_WIDTH);

		while(dx < v.getWidth())
		{
			double x = v.getX() - dx;
			if (Math.abs(x) < v.getUnitScale() / 2)
			{
				g.save();
				g.setLineWidth(AXIS_LINE_WIDTH);
				g.setStroke(COLOR_YAXIS);
				g.strokeLine(dx, 0, dx, v.getHeight());
				g.restore();
			}
			else
			{
				g.strokeLine(dx, 0, dx, v.getHeight());
			}
			dx += v.getUnitScale();
		}

		while(dy < v.getHeight())
		{
			double y = v.getY() - dy;
			if (Math.abs(y) < v.getUnitScale() / 2)
			{
				g.save();
				g.setLineWidth(AXIS_LINE_WIDTH);
				g.setStroke(COLOR_XAXIS);
				g.strokeLine(0, dy, v.getWidth(), dy);
				g.restore();
			}
			else
			{
				g.strokeLine(0, dy, v.getWidth(), dy);
			}
			dy += v.getUnitScale();
		}
	}
}
