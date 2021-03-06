package boron.stage_a.goblet.tile;

import boron.base.gridmap.IntMap;
import boron.stage_a.base.collisionbox.box.GridBasedBoundingBox;
import boron.stage_a.base.collisionbox.collider.BoxCollider;

/**
 * Created by Andy on 9/1/17.
 */
public class TileMap implements BoxCollider
{
	protected final GridBasedBoundingBox boundingBox;
	protected final IntMap tiles;

	protected final int offsetX;
	protected final int offsetY;

	public TileMap(int offsetX, int offsetY, int width, int height)
	{
		this.tiles = new IntMap(width, height);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.boundingBox = new GridBasedBoundingBox(this.offsetX, this.offsetY, width, height);
	}

	public boolean isWithinBounds(float x, float y)
	{
		return x >= this.offsetX && y >= this.offsetY && x < this.offsetX + this.tiles.getWidth() && y < this.offsetY + this.tiles.getHeight();
	}

	public Tile getTile(float x, float y)
	{
		int ix = (int) Math.floor(x - this.offsetX);
		int iy = (int) Math.floor(y - this.offsetY);
		return this.getTileByMap(ix, iy);
	}

	public void setTile(float x, float y, Tile tile)
	{
		int ix = (int) Math.floor(x - this.offsetX);
		int iy = (int) Math.floor(y - this.offsetY);
		this.setTileByMap(ix, iy, tile);
	}

	public Tile getTileByMap(int ix, int iy)
	{
		int id = 0;
		if (ix >= 0 && iy >= 0 && ix < this.getWidth() && iy < this.getHeight())
		{
			id = this.tiles.get(ix, iy);
		}

		return Tile.getTileByID(id);
	}

	public void setTileByMap(int ix, int iy, Tile tile)
	{
		this.tiles.put(ix, iy, tile.getID());
		this.boundingBox.setSolid(ix + iy * this.getWidth(), tile.isSolid());
	}

	public int getOffsetX()
	{
		return this.offsetX;
	}

	public int getOffsetY()
	{
		return this.offsetY;
	}

	public int getWidth()
	{
		return this.tiles.getWidth();
	}

	public int getHeight()
	{
		return this.tiles.getHeight();
	}

	@Override
	public GridBasedBoundingBox getBoundingBox()
	{
		return this.boundingBox;
	}
}
