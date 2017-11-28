package net.jimboi.test.suger.dungeon.slot;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * Created by Andy on 11/22/17.
 */
public class DungeonSlotGroup
{
	private final DungeonSlot[] slots;

	public DungeonSlotGroup(DungeonSlot... slots)
	{
		this.slots = slots;

		for(DungeonSlot slot : this.slots)
		{
			slot.group = this;
		}
	}

	public boolean input(KeyCode code)
	{
		for(DungeonSlot slot : this.slots)
		{
			if (slot.getHotkey() == code && this.canActivateSlot(slot))
			{
				slot.activate();
				return true;
			}
		}

		return false;
	}

	protected boolean canActivateSlot(DungeonSlot slot)
	{
		return true;
	}

	public void onSlotActivate(DungeonSlot slot)
	{}

	public void render(GraphicsContext g)
	{
		for(DungeonSlot slot : this.slots)
		{
			g.save();
			slot.render(g);
			g.restore();
		}
	}

	public DungeonSlot[] getSlots()
	{
		return this.slots;
	}
}
