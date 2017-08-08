package net.jimboi.boron.stage_a.smack.aabb;

/**
 * Created by Andy on 8/7/17.
 */
public class GridBasedBoundingMap extends Box
{
	protected int offsetX;
	protected int offsetY;
	protected int width;
	protected int height;

	protected boolean[] solids;

	public GridBasedBoundingMap(int offsetX, int offsetY, int width, int height)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;

		this.solids = new boolean[this.width * this.height];
	}

	public void offset(int x, int y)
	{
		this.offsetX += x;
		this.offsetY += y;
	}

	public void setOffset(int offsetX, int offsetY)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public void setSolid(int index, boolean solid)
	{
		this.solids[index] = solid;
	}

	public boolean isSolid(int index)
	{
		return this.solids[index];
	}

	public float getOffsetX()
	{
		return this.offsetX;
	}

	public float getOffsetY()
	{
		return this.offsetY;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}
}
