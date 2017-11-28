package net.jimboi.test.suger.dungeon.slot;

import net.jimboi.test.suger.Renderer;
import net.jimboi.test.suger.dungeon.DungeonTileRenderer;
import net.jimboi.test.suger.dungeon.tile.DungeonTile;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Andy on 11/22/17.
 */
public class SlotTile extends DungeonSlot
{
	private final DungeonTileRenderer renderer = new DungeonTileRenderer();

	private DungeonTile tile;

	public SlotTile(DungeonTile tile)
	{
		this.tile = tile;
	}

	public SlotTile setTile(DungeonTile tile)
	{
		this.tile = tile;
		return this;
	}

	@Override
	protected void onActivate()
	{
	}

	@Override
	protected void onRender(GraphicsContext g)
	{
		this.renderer.direction = -1;
		double borderW = this.width / 5;
		double borderH = this.height / 5;
		Renderer.drawTile(g, this.renderer, this.tile,
				this.x + borderW, this.y + borderH,
				this.width - borderW * 2, this.height - borderH * 2);
	}

	public DungeonTile getTile()
	{
		return this.tile;
	}
}
