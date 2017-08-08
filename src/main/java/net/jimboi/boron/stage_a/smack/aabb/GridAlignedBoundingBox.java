package net.jimboi.boron.stage_a.smack.aabb;

/**
 * Created by Andy on 8/7/17.
 */
public class GridAlignedBoundingBox extends AxisAlignedBoundingBox
{
	public GridAlignedBoundingBox(int offsetX, int offsetY, int width, int height)
	{
		super(offsetX + width / 2F, offsetY + height / 2F, width / 2F, height / 2F);
	}

	public void setOffset(int offsetX, int offsetY)
	{
		this.centerX = offsetX + this.halfWidth;
		this.centerY = offsetY + this.halfHeight;
	}
}
