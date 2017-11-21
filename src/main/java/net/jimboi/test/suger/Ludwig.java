package net.jimboi.test.suger;

import net.jimboi.test.suger.baron.Baron;
import net.jimboi.test.suger.baron.InputHandler;
import net.jimboi.test.suger.baron.RenderHandler;
import net.jimboi.test.suger.baron.ViewPort;
import net.jimboi.test.suger.baron.WorldHandler;
import net.jimboi.test.suger.canvas.LayeredCanvasPane;
import net.jimboi.test.suger.dungeon.Dungeon;
import net.jimboi.test.suger.dungeon.DungeonHandler;
import net.jimboi.test.suger.dungeon.DungeonTileRenderer;
import net.jimboi.test.suger.dungeon.tile.DungeonTile;
import net.jimboi.test.suger.dungeon.tile.DungeonTiles;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by Andy on 11/13/17.
 */
public class Ludwig extends Application implements InputHandler, WorldHandler, RenderHandler
{
	private final Renderer renderer = new Renderer();

	private int gridMode = 0;

	private int tileMode = 0;
	private int tile = DungeonTiles.DOOR.getTileID() + 1;

	private boolean showExport = false;
	private boolean export = false;

	private boolean dirty = false;
	private int tick = 5;

	private Dungeon dungeon;
	private DungeonHandler dungeonHandler;

	private Vector2f prevMouse = new Vector2f();
	private Vector2f nextMouse = new Vector2f();

	@Override
	public void onCreate()
	{
		this.dungeon = new Dungeon();
		this.dungeonHandler = new DungeonHandler(this.dungeon);

		this.dungeon.getMap().loadChunk(new Vector2i(0, 0));
	}

	@Override
	public void onUpdate()
	{
		if (this.dirty && this.tick-- < 0)
		{
			this.dungeonHandler.solveRegions();
			this.dungeonHandler.solveDirectionsByRegion();
			this.tick = 5;
			this.dirty = false;
		}
	}

	@Override
	public void onInput(KeyCode code)
	{
		switch (code)
		{
			case W:
				this.tileMode = 0;
				break;
			case S:
				this.tileMode = 1;
				break;
			case D:
				if (this.tileMode != 2)
				{
					this.tileMode = 2;
				}
				else
				{
					++this.tile;
					if (this.tile >= DungeonTile.getNumOfRegisteredTiles())
					{
						this.tile = DungeonTiles.DOOR.getTileID() + 1;
					}
				}
				break;
			case A:
				this.tileMode = 3;
				break;
			case P:
				if (++this.gridMode > 2)
				{
					this.gridMode = 0;
				}
				break;
			case E:
				if (this.tileMode != 2)
				{
					this.dungeon.setBlockTile(this.dungeon.getRegion(this.nextMouse.x(), this.nextMouse.y()), null);
				}
				else
				{
					this.dungeon.setBlockTile(this.dungeon.getRegion(this.nextMouse.x(), this.nextMouse.y()), DungeonTile.getTileByID(this.tile));
				}
				break;
			case O:
				this.export = true;
				break;
		}
	}

	@Override
	public void onDraw(LayeredCanvasPane canvas, ViewPort view)
	{
		canvas.clear();

		final double width = canvas.getWidth();
		final double height = canvas.getHeight();
		view.setSize(width, height);

		this.prevMouse.set(this.nextMouse);
		ViewPort.getWorldPos(view, view.getCursorX(), view.getCursorY(), this.nextMouse);

		if (view.isCursorDown())
		{
			float x = this.nextMouse.x();
			float y = this.nextMouse.y();

			if (this.tileMode == 0)
			{
				this.dungeon.setItem(x, y, DungeonTiles.AIR);
				this.dungeon.setSolid(x, y, true);
				this.dungeon.setDirection(x, y, (byte) 0);
				this.dungeon.setRegion(x, y, 0);
			}
			else if (this.tileMode == 1)
			{
				this.dungeon.setItem(x, y, DungeonTiles.AIR);
				this.dungeon.setSolid(x, y, false);
				this.dungeon.setDirection(x, y, (byte) 15);
				this.dungeon.setRegion(x, y, 1);
				this.dirty = true;
			}
			else if (this.tileMode == 2)
			{
				this.dungeon.setItem(x, y, DungeonTile.getTileByID(this.tile));
			}
			else if (this.tileMode == 3)
			{
				this.dungeon.setItem(x, y, DungeonTiles.DOOR);
			}

			this.dirty = true;
		}

		GraphicsContext gui = canvas.getCanvas(Renderer.LAYER_GUI).getGraphicsContext2D();
		this.renderer.drawDungeon(canvas, view, this.dungeon);

		if (this.gridMode == 1)
		{
			this.renderer.drawGrid(gui, view);
		}

		if (this.export)
		{
			Image image = this.dungeonHandler.exportToImage(canvas, view);

			File file = new File("img.png");

			try
			{
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				this.export = false;
			}
		}
		else
		{
			if (this.gridMode == 2)
			{
				Rectangle2D area = this.dungeonHandler.getExportArea(view);
				this.renderer.drawExportArea(gui, area);
				double w = area.getWidth() / view.getUnitScale();
				double h = area.getHeight() / view.getUnitScale();
				int i = (int) Math.floor(w);
				int j = (int) Math.floor(h);
				gui.save();

				Font font = Font.font(20);
				gui.setFont(font);

				String text = i + "x" + j;
				double x = area.getMinX();
				double y = area.getMaxY();
				gui.setEffect(new DropShadow(5, Color.BLACK));
				gui.setFill(Color.WHITE);
				gui.fillText(text, x + 10, y - 10);

				gui.restore();
			}

			this.renderer.drawCursor(gui, view, this.nextMouse);
			this.renderer.drawSlot(gui, this.tileMode, 10, 10, 44, 44);
			DungeonTileRenderer renderer = new DungeonTileRenderer();
			renderer.direction = -1;
			this.renderer.drawTile(gui, renderer,
					this.tileMode == 0 ? DungeonTiles.PLACEHOLDER :
							this.tileMode == 1 ? DungeonTiles.AIR :
									this.tileMode == 3 ? DungeonTiles.DOOR :
											DungeonTile.getTileByID(this.tile),
					18, 18,
					28, 28);
		}
	}

	@Override
	public int getNumOfLayers()
	{
		return Renderer.LAYERS;
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Baron.initialize(new ViewPort(),
				this, this, this,
				primaryStage);
	}

	public static void main(String[] args)
	{
		Application.launch(args);
	}
}
