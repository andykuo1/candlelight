package net.jimboi.boron.stage_a.goblet;

import net.jimboi.boron.stage_a.base.collisionbox.box.GridBasedBoundingBox;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;

import org.qsilver.util.map2d.IntMap;

/**
 * Created by Andy on 8/10/17.
 */
public class Room implements BoxCollider
{
	protected final GridBasedBoundingBox boundingBox;
	protected final IntMap tilemap;
	protected final int offsetX;
	protected final int offsetY;

	public Room(int offsetX, int offsetY, int width, int height)
	{
		this.tilemap = new IntMap(width, height);
		this.offsetX = offsetX;
		this.offsetY = offsetY;

		for(int i = 0; i < this.tilemap.size(); ++i)
		{
			this.tilemap.set(i, Math.random() < 0.2F ? 1 : 0);
		}

		this.boundingBox = new GridBasedBoundingBox(this.offsetX, this.offsetY, width, height);

		for(int i = 0; i < this.tilemap.size(); ++i)
		{
			this.boundingBox.setSolid(i, this.tilemap.get(i) == 1);
		}
	}

	public int getTile(float x, float y)
	{
		if (x < this.offsetX || y < this.offsetY || x >= this.offsetX + this.tilemap.width || y >= this.offsetY + this.tilemap.height) return 0;

		return this.tilemap.get((int) Math.floor(x + this.offsetX), (int) (Math.floor(y + this.offsetY)));
	}

	public boolean isSolid(float x, float y)
	{
		int tile = this.getTile(x, y);
		return tile == 1;
	}

	@Override
	public GridBasedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
