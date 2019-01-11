package canary.test.tilemapper.suger;

import canary.test.tilemapper.suger.baron.Baron;
import canary.test.tilemapper.suger.baron.InputHandler;
import canary.test.tilemapper.suger.baron.RenderHandler;
import canary.test.tilemapper.suger.baron.ViewPort;
import canary.test.tilemapper.suger.canvas.LayeredCanvas;
import canary.test.tilemapper.suger.dungeon.Dungeon;
import canary.test.tilemapper.suger.dungeon.DungeonHandler;
import canary.test.tilemapper.suger.dungeon.slot.DungeonSlot;
import canary.test.tilemapper.suger.dungeon.slot.DungeonSlotGroup;
import canary.test.tilemapper.suger.dungeon.slot.SlotButton;
import canary.test.tilemapper.suger.dungeon.slot.SlotGroupTileSelector;
import canary.test.tilemapper.suger.dungeon.tile.DungeonTiles;

import canary.bstone.json.JSON;
import canary.bstone.json.JSONObject;
import canary.bstone.util.parser.json.JSONFormatParser;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by Andy on 11/13/17.
 */
public class Ludwig extends Application implements InputHandler, RenderHandler
{
	private boolean draftMode = true;
	private boolean helpMode = false;
	private int gridMode = 0;
	private boolean export = false;

	private boolean dirty = false;
	private int tick = 5;

	private Dungeon dungeon;
	private DungeonHandler dungeonHandler;

	private Set<DungeonSlotGroup> slotGroups = new HashSet<>();
	private DungeonSlot slotFocus = null;

	private boolean prevDown = false;
	private Vector2f prevMouse = new Vector2f();
	private Vector2f nextMouse = new Vector2f();

	private SlotGroupTileSelector selector;

	@Override
	public void onLoad(Stage stage, LayeredCanvas canvas, ViewPort view)
	{
		final double width = canvas.getWidth();
		final double height = canvas.getHeight();

		this.slotGroups.add(this.selector = new SlotGroupTileSelector());

		final int slotWidth = SlotButton.WIDTH;
		final int slotHeight = SlotButton.HEIGHT;

		int buttonIndex = 0;

		this.slotGroups.add(new DungeonSlotGroup(new SlotButton("NEW",
				() -> this.newDungeon(stage))
				.setHotkey(KeyCode.O)
				.setPosition(width - slotWidth - 10,
						10 + (slotHeight + 8) * buttonIndex++)));

		this.slotGroups.add(new DungeonSlotGroup(new SlotButton("SAVE",
				() -> this.saveDungeon(stage))
				.setHotkey(KeyCode.O)
				.setPosition(width - slotWidth - 10,
						10 + (slotHeight + 8) * buttonIndex++)));

		this.slotGroups.add(new DungeonSlotGroup(new SlotButton("LOAD",
				() -> this.loadDungeon(stage))
				.setHotkey(KeyCode.O)
				.setPosition(width - slotWidth - 10,
						10 + (slotHeight + 8) * buttonIndex++)));

		this.slotGroups.add(new DungeonSlotGroup(new SlotButton("EXPORT",
				() -> this.export = true)
				.setHotkey(KeyCode.O)
				.setPosition(width - slotWidth - 10,
						10 + (slotHeight + 8) * buttonIndex++)));
		this.slotGroups.add(new DungeonSlotGroup(new SlotButton("GRID",
				() ->
				{
					if (++this.gridMode > 2) this.gridMode = 0;
				})
				.setHotkey(KeyCode.P)
				.setPosition(width - slotWidth - 10,
						10 + (slotHeight + 8) * buttonIndex++)));

		this.slotGroups.add(new DungeonSlotGroup(new SlotButton("DRAFT",
				() -> this.draftMode = !this.draftMode)
				.setHotkey(KeyCode.L)
				.setPosition(width - slotWidth - 10,
						10 + (slotHeight + 8) * buttonIndex++)));

		this.slotGroups.add(new DungeonSlotGroup(new SlotButton("HELP",
				() -> this.helpMode = !this.helpMode)
				.setHotkey(KeyCode.K)
				.setPosition(width - slotWidth - 10,
						10 + (slotHeight + 8) * buttonIndex++)));

		this.newDungeon(stage);
	}

	private void newDungeon(Stage stage)
	{
		this.dungeon = new Dungeon();
		this.dungeonHandler = new DungeonHandler(this.dungeon);
		this.dungeon.getMap().loadChunk(new Vector2i(0, 0));
	}

	private void saveDungeon(Stage stage)
	{
		FileChooser chooser = new FileChooser();
		chooser.setInitialFileName("dungeon.json");
		File file = chooser.showSaveDialog(stage);

		if (file != null)
		{
			try
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				JSONObject data = Dungeon.writeToJSON(this.dungeon);
				JSON.write(writer, data);
				writer.flush();
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void loadDungeon(Stage stage)
	{
		FileChooser chooser = new FileChooser();
		chooser.setInitialFileName("dungeon.json");
		File file = chooser.showOpenDialog(stage);

		if (file != null)
		{
			try
			{
				JSONObject data;
				BufferedReader reader = new BufferedReader(new FileReader(file));
				JSONFormatParser parser = new JSONFormatParser(256);
				data = (JSONObject) parser.parse(reader);
				reader.close();
				this.dungeon = Dungeon.loadFromJSON(data);
				this.dungeonHandler = new DungeonHandler(this.dungeon);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onInput(KeyCode code)
	{
		switch (code)
		{
			case E:
				if (this.selector.getTileMode() != 2)
				{
					this.dungeon.setBlockTile(this.dungeon.getRegion(this.nextMouse.x(), this.nextMouse.y()), null);
				}
				else
				{
					this.dungeon.setBlockTile(this.dungeon.getRegion(this.nextMouse.x(), this.nextMouse.y()), this.selector.getTile());
				}
				break;
			default:
				for(DungeonSlotGroup group : this.slotGroups)
				{
					if (group.input(code)) break;
				}
		}
	}

	@Override
	public void onDraw(Stage stage, LayeredCanvas canvas, ViewPort view)
	{
		if (this.dirty && this.tick-- < 0)
		{
			this.dungeonHandler.solveRegions();
			this.dungeonHandler.solveDirectionsByRegion();
			this.tick = 5;
			this.dirty = false;
		}

		canvas.clear();
		final double width = canvas.getWidth();
		final double height = canvas.getHeight();
		view.setSize(width, height);
		final double mousex = view.getCursorX();
		final double mousey = view.getCursorY();

		this.prevMouse.set(this.nextMouse);
		ViewPort.getWorldPos(view, mousex, mousey, this.nextMouse);

		this.slotFocus = null;
		for(DungeonSlotGroup group : this.slotGroups)
		{
			for(DungeonSlot slot : group.getSlots())
			{
				if (slot.contains(mousex, mousey))
				{
					this.slotFocus = slot;
				}
			}
		}

		if (view.isCursorDown())
		{
			this.prevDown = true;

			if (this.slotFocus == null)
			{
				float x = this.nextMouse.x();
				float y = this.nextMouse.y();

				if (this.selector.getTileMode() == 1)
				{
					this.dungeon.setItem(x, y, DungeonTiles.AIR);
					this.dungeon.setSolid(x, y, true);
					this.dungeon.setDirection(x, y, (byte) 0);
					this.dungeon.setRegion(x, y, 0);
				}
				else if (this.selector.getTileMode() == 0)
				{
					this.dungeon.setItem(x, y, DungeonTiles.AIR);
					this.dungeon.setSolid(x, y, false);
					this.dungeon.setDirection(x, y, (byte) 15);
					this.dungeon.setRegion(x, y, 1);
					this.dirty = true;
				}
				else if (this.selector.getTileMode() == 2)
				{
					this.dungeon.setItem(x, y, this.selector.getTile());
				}
				else if (this.selector.getTileMode() == 3)
				{
					this.dungeon.setItem(x, y, DungeonTiles.DOOR);
				}

				this.dirty = true;
			}
		}
		else if (this.prevDown)
		{
			this.prevDown = false;

			if (this.slotFocus != null)
			{
				this.slotFocus.activate();
			}
		}

		GraphicsContext gui = canvas.getCanvas(Renderer.LAYER_GUI).getGraphicsContext2D();
		Renderer.drawDungeon(canvas, view, this.dungeon, this.draftMode);

		if (this.gridMode == 1)
		{
			Renderer.drawGrid(gui, view);
		}

		if (this.export)
		{
			FileChooser chooser = new FileChooser();
			chooser.setInitialFileName("dungeon.png");
			File file = chooser.showSaveDialog(stage);

			if (file != null)
			{
				Image image = this.dungeonHandler.exportToImage(canvas, view);

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
				this.export = false;
			}
		}
		else
		{
			if (this.gridMode == 2)
			{
				Rectangle2D area = this.dungeonHandler.getExportArea(view);
				Renderer.drawExportArea(gui, area);
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

			if (this.slotFocus == null)
			{
				Renderer.drawCursor(gui, view, this.nextMouse);
			}

			for(DungeonSlotGroup group : this.slotGroups)
			{
				group.render(gui);
				if (this.helpMode)
				{
					for(DungeonSlot slot : group.getSlots())
					{
						slot.renderHotkey(gui);
					}
				}
			}
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
				this, this,
				primaryStage);
	}

	public static void main(String[] args)
	{
		Application.launch(args);
	}
}
