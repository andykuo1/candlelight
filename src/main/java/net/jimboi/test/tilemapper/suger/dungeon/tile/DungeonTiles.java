package net.jimboi.test.tilemapper.suger.dungeon.tile;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

/**
 * Created by Andy on 11/15/17.
 */
public class DungeonTiles
{
	public static final DungeonTile AIR = DungeonTile.register(0, new TileAir());
	public static final DungeonTile PLACEHOLDER = DungeonTile.register(1, new TileBlock());
	public static final DungeonTile DOOR = DungeonTile.register(2, new TileItemDoor()
			//.setDoorPaint(Color.SADDLEBROWN));
			.setDoorPaint(new ImagePattern(new Image("dungeon/tex4.jpg"))));
	public static final DungeonTile STONE = DungeonTile.register(3, new TileBlock()
			//.setBlockPaint(Color.SLATEGRAY));
			.setBlockPaint(new ImagePattern(new Image("dungeon/tex2.jpg"))));
	public static final DungeonTile DIRT = DungeonTile.register(4, new TileBlock()
			//.setBlockPaint(Color.SADDLEBROWN.darker().darker()));
			.setBlockPaint(new ImagePattern(new Image("dungeon/tex7.jpg"))));
	public static final DungeonTile CHEST = DungeonTile.register(5, new TileItemChest()
			//.setChestPaint(Color.SADDLEBROWN.brighter())
			//.setOutlinePaint(Color.SADDLEBROWN.darker().darker()));
			.setChestPaint(new ImagePattern(new Image("dungeon/tex3.jpg")))
			.setOutlinePaint(Color.SADDLEBROWN.darker().darker()));
	public static final DungeonTile STAIRS = DungeonTile.register(6, new TileItemStairs()
			.setStairPaint(new ImagePattern(new Image("dungeon/tex9.jpg"))));
			//.setStairPaint(Color.SLATEGRAY.brighter())
	public static final DungeonTile SMALL_FURNITURE = DungeonTile.register(7, new TileItemFurniture(0.4)
			//.setMainPaint(Color.AQUA.brighter())
			//.setOutlinePaint(Color.SADDLEBROWN.darker().darker()));
			.setMainPaint(Color.ALICEBLUE.darker())
			.setOutlinePaint(Color.ALICEBLUE.darker().darker()));
	public static final DungeonTile LARGE_FURNITURE = DungeonTile.register(8, new TileItemFurniture(0.7)
			//.setMainPaint(Color.AQUA.brighter())
			//.setOutlinePaint(Color.SADDLEBROWN.darker().darker()));
			.setMainPaint(Color.ALICEBLUE.darker())
			.setOutlinePaint(Color.ALICEBLUE.darker().darker()));
	public static final DungeonTile TILED_FLOOR = DungeonTile.register(9, new TileBlock()
			.setBlockPaint(new ImagePattern(new Image("dungeon/tex1.jpg"))));
	public static final DungeonTile SLATE_DIRT = DungeonTile.register(10, new TileBlock()
			.setBlockPaint(new ImagePattern(new Image("dungeon/tex5.jpg"))));
	public static final DungeonTile GOLD_DIRT = DungeonTile.register(11, new TileBlock()
			.setBlockPaint(new ImagePattern(new Image("dungeon/tex8.jpg"))));
}
