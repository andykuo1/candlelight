package net.jimboi.boron.stage_a.base.collisionbox.box;

/**
 * Created by Andy on 8/7/17.
 */
public class AxisAlignedBoundingBox extends BoundingBox
{
	protected float centerX;
	protected float centerY;
	protected float halfWidth;
	protected float halfHeight;

	public AxisAlignedBoundingBox(float centerX, float centerY, float halfWidth, float halfHeight)
	{
		this.centerX = centerX;
		this.centerY = centerY;
		this.halfWidth = halfWidth;
		this.halfHeight = halfHeight;
	}

	public void expand(float w, float h)
	{
		this.halfWidth += w / 2;
		this.halfHeight += h / 2;
	}

	public void offset(float x, float y)
	{
		this.centerX += x;
		this.centerY += y;
	}

	public void setCenter(float centerX, float centerY)
	{
		this.centerX = centerX;
		this.centerY = centerY;
	}

	public void setHalfSize(float halfWidth, float halfHeight)
	{
		this.halfWidth = halfWidth;
		this.halfHeight = halfHeight;
	}

	@Override
	public boolean isWithinRange(float x, float y, float radius)
	{
		return x + radius >= this.centerX - this.halfWidth && x - radius <= this.centerX + this.halfWidth && y + radius >= this.centerY - this.halfHeight && y - radius <= this.centerY + this.halfHeight;
	}

	public float getCenterX()
	{
		return this.centerX;
	}

	public float getCenterY()
	{
		return this.centerY;
	}

	public float getHalfWidth()
	{
		return this.halfWidth;
	}

	public float getHalfHeight()
	{
		return this.halfHeight;
	}
}
