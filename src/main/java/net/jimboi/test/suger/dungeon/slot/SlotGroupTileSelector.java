package net.jimboi.test.suger.dungeon.slot;

import net.jimboi.test.suger.dungeon.tile.DungeonTile;
import net.jimboi.test.suger.dungeon.tile.DungeonTiles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Created by Andy on 11/22/17.
 */
public class SlotGroupTileSelector extends DungeonSlotGroup
{
	private static final Color COLOR_SLOT_BLOCK = Color.CORNFLOWERBLUE;
	private static final Color COLOR_SLOT_AIR = Color.INDIANRED;
	private static final Color COLOR_SLOT_ITEM = Color.LIGHTGRAY;

	private SlotTile air;
	private SlotTile solid;
	private SlotTile tiles;
	private SlotTile door;

	private int tileMode;
	private int tile = DungeonTiles.DOOR.getTileID() + 1;

	public SlotGroupTileSelector()
	{
		super(new SlotTile(DungeonTiles.AIR)
						.setColor(COLOR_SLOT_AIR).setHotkey(KeyCode.S),
				new SlotTile(DungeonTiles.PLACEHOLDER)
						.setColor(COLOR_SLOT_BLOCK).setHotkey(KeyCode.W),
				new SlotTile(DungeonTile.getTileByID(DungeonTiles.DOOR.getTileID() + 1))
						.setColor(COLOR_SLOT_ITEM).setHotkey(KeyCode.D),
				new SlotTile(DungeonTiles.DOOR)
						.setColor(COLOR_SLOT_ITEM).setHotkey(KeyCode.A));

		this.air = (SlotTile) this.getSlots()[0];
		this.solid = (SlotTile) this.getSlots()[1];
		this.tiles = (SlotTile) this.getSlots()[2];
		this.door = (SlotTile) this.getSlots()[3];

		this.setTileMode(0);
	}

	@Override
	protected boolean canActivateSlot(DungeonSlot slot)
	{
		if (slot == this.air)
		{
			this.setTileMode(0);
		}
		else if (slot == this.solid)
		{
			this.setTileMode(1);
		}
		else if (slot == this.tiles)
		{
			if (this.tileMode == 2)
			{
				++this.tile;
				if (this.tile >= DungeonTile.getNumOfRegisteredTiles())
				{
					this.tile = DungeonTiles.DOOR.getTileID() + 1;
				}

				this.tiles.setTile(this.getTile());
			}
			else
			{
				this.setTileMode(2);
			}
		}
		else if (slot == this.door)
		{
			this.setTileMode(3);
		}

		return false;
	}

	@Override
	public void onSlotActivate(DungeonSlot slot)
	{
		this.canActivateSlot(slot);
	}

	@Override
	public void render(GraphicsContext g)
	{
		super.render(g);
	}

	public void setTileMode(int mode)
	{
		this.tileMode = mode;

		if (this.tileMode == 0)
		{
			this.makeActiveSlot(this.air);
			this.makeInactiveSlot(this.solid, 0);
			this.makeInactiveSlot(this.tiles, 1);
			this.makeInactiveSlot(this.door, 2);
		}
		else if (this.tileMode == 1)
		{
			this.makeActiveSlot(this.solid);
			this.makeInactiveSlot(this.air, 0);
			this.makeInactiveSlot(this.tiles, 1);
			this.makeInactiveSlot(this.door, 2);
		}
		else if (this.tileMode == 2)
		{
			this.makeActiveSlot(this.tiles);
			this.makeInactiveSlot(this.air, 0);
			this.makeInactiveSlot(this.solid, 1);
			this.makeInactiveSlot(this.door, 2);
		}
		else if (this.tileMode == 3)
		{
			this.makeActiveSlot(this.door);
			this.makeInactiveSlot(this.air, 0);
			this.makeInactiveSlot(this.solid, 1);
			this.makeInactiveSlot(this.tiles, 2);
		}
	}

	private void makeActiveSlot(DungeonSlot slot)
	{
		slot.setPosition(10, 10);
		slot.setSize(44, 44);
		slot.setEnabled(true);
	}

	private void makeInactiveSlot(DungeonSlot slot, int index)
	{
		slot.setPosition(60, 10 + 32 * index);
		slot.setSize(24, 24);
		slot.setEnabled(false);
	}

	public int getTileMode()
	{
		return this.tileMode;
	}

	public DungeonTile getTile()
	{
		return DungeonTile.getTileByID(this.tile);
	}
}
