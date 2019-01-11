package canary.zilar.gui;

import canary.bstone.util.Direction;
import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 11/28/17.
 */
public class Gui extends GuiBase
{
	protected Direction anchor = Direction.NORTHWEST;
	protected float offsetX;
	protected float offsetY;

	protected float width = 1;
	protected float height = 1;
	protected float aspectRatio = 1;

	protected float widthRatio = 0;
	protected float heightRatio = 0;

	protected boolean isVerticallyCentered;
	protected boolean isHorizontallyCentered;

	public Gui setAnchorDirection(Direction direction)
	{
		this.anchor = direction;
		return this;
	}

	public Gui setSize(float width, float height)
	{
		this.width = width;
		this.height = height;
		this.aspectRatio = this.width / this.height;
		return this;
	}

	public Gui setOffset(float x, float y)
	{
		this.offsetX = x;
		this.offsetY = y;
		return this;
	}

	public Gui setRelativeWidth(float ratio)
	{
		this.widthRatio = ratio;
		return this;
	}

	public Gui setRelativeHeight(float ratio)
	{
		this.heightRatio = ratio;
		return this;
	}

	@Override
	protected void update()
	{
		if (this.parent != null)
		{
			//Move top-left to parent's top-left
			this.screenX = this.parent.screenX;
			this.screenY = this.parent.screenY;

			//Update size
			if (this.widthRatio == 0 && this.heightRatio == 0)
			{
				this.screenW = this.width;
				this.screenH = this.height;
			}
			else
			{
				if (this.widthRatio != 0)
				{
					this.screenW = this.parent.screenW * this.widthRatio;

					if (this.heightRatio == 0)
					{
						this.screenH = this.screenW / this.aspectRatio;
					}
				}
				if (this.heightRatio != 0)
				{
					this.screenH = this.parent.screenH * this.heightRatio;

					if (this.widthRatio == 0)
					{
						this.screenH = this.screenH * this.aspectRatio;
					}
				}
			}

			final Vector2f vec = new Vector2f();

			//Apply parent offset by anchor
			{
				Vector2fc parentOffset = getNormalizedAnchorOffset(this.anchor, vec)
						.mul(this.parent.screenW, this.parent.screenH);
				this.screenX += parentOffset.x();
				this.screenY += parentOffset.y();
			}

			//Apply element offset by anchor
			{
				Vector2fc offset = getNormalizedAnchorOffset(this.anchor, vec)
						.mul(this.screenW, this.screenH);

				if (this.isVerticallyCentered)
				{
					this.screenY -= this.screenH / 2F;
				}
				else
				{
					this.screenY -= offset.y();
				}

				if (this.isHorizontallyCentered)
				{
					this.screenX -= this.screenW / 2F;
				}
				else
				{
					this.screenX -= offset.x();
				}
			}

			//Apply x offset
			if (this.anchor.isEast()) //Inverted
			{
				this.screenX -= this.offsetX;
			}
			else
			{
				this.screenX += this.offsetX;
			}

			//Apply y offset
			if (this.anchor.isSouth()) //Inverted
			{
				this.screenY -= this.offsetY;
			}
			else
			{
				this.screenY += this.offsetY;
			}
		}
		else
		{
			this.screenW = this.width;
			this.screenH = this.height;

			if (this.isVerticallyCentered)
			{
				this.screenY = -this.screenH / 2F;
			}
			else
			{
				this.screenY = 0;
			}

			if (this.isHorizontallyCentered)
			{
				this.screenX = -this.screenW / 2F;
			}
			else
			{
				this.screenX = 0;
			}
		}

		super.update();
	}

	public Direction getAnchorDirection()
	{
		return this.anchor;
	}

	public static Vector2f getNormalizedAnchorOffset(Direction anchor, Vector2f dst)
	{
		switch (anchor)
		{
			case EAST:
				return dst.set(1, 0.5F);
			case NORTHEAST:
				return dst.set(1, 0);
			case NORTH:
				return dst.set(0.5F, 0);
			case NORTHWEST:
				return dst.set(0, 0);
			case WEST:
				return dst.set(0, 0.5F);
			case SOUTHWEST:
				return dst.set(0, 1);
			case SOUTH:
				return dst.set(0.5F, 1);
			case SOUTHEAST:
				return dst.set(1, 1);
			case CENTER:
				return dst.set(0.5F, 0.5F);
		}

		return dst.set(0, 0);
	}
}
