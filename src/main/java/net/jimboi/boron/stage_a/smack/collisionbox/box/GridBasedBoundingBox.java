package net.jimboi.boron.stage_a.smack.collisionbox.box;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 8/7/17.
 */
public class GridBasedBoundingBox extends BoundingBox
{
	protected int offsetX;
	protected int offsetY;
	protected int width;
	protected int height;

	protected boolean[] solids;
	protected GridAlignedBoundingBox boundingBox;

	public GridBasedBoundingBox(int offsetX, int offsetY, int width, int height)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.width = width;
		this.height = height;

		this.solids = new boolean[this.width * this.height];
		this.boundingBox = new GridAlignedBoundingBox(this.offsetX, this.offsetY, 1, 1);
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

	public boolean isSolid(float x, float y)
	{
		float offsetX = x - this.offsetX;
		float offsetY = y - this.offsetY;

		if (offsetX < 0 || offsetY < 0 || offsetX >= this.width || offsetY >= this.height) return false;

		return this.isSolid((int) Math.floor(offsetX) + ((int) Math.floor(offsetY)) * this.width);
	}

	public List<GridAlignedBoundingBox> getSubBox(float x, float y, float radiusX, float radiusY)
	{
		List<GridAlignedBoundingBox> boxes = new ArrayList<>();

		float offsetX = x - this.offsetX;
		float offsetY = y - this.offsetY;

		if (offsetX + radiusX < 0 || offsetY + radiusY < 0 || offsetX - radiusX >= this.width || offsetY - radiusY >= this.height) return boxes;

		int fromX = Math.max((int) Math.floor(offsetX - radiusX), 0);
		int fromY = Math.max((int) Math.floor(offsetY - radiusY), 0);
		int toX = Math.min((int) Math.ceil(offsetX + radiusX), this.width);
		int toY = Math.min((int) Math.ceil(offsetY + radiusY), this.height);

		for(int x1 = fromX; x1 < toX; ++x1)
		{
			for (int y1 = fromY; y1 < toY; ++y1)
			{
				if (this.isSolid(x1 + y1 * this.width))
				{
					boxes.add(new GridAlignedBoundingBox(this.offsetX + x1, this.offsetY + y1, 1, 1));
				}
			}
		}

		return boxes;
	}

	public GridAlignedBoundingBox getSubBox(float x, float y)
	{
		if (!this.isSolid(x, y)) return null;

		return new GridAlignedBoundingBox((int) Math.floor(x) + this.offsetX, (int) Math.floor(y) + this.offsetY, 1, 1);
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
