package net.jimboi.boron.stage_a.smack.aabb;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 8/6/17.
 */
public class AxisAlignedBoundingBox
{
	private final Vector2f position = new Vector2f();
	private final Vector2f size = new Vector2f();

	public AxisAlignedBoundingBox(float x, float y, float width, float height)
	{
		this.position.set(x, y);
		this.size.set(width, height);
	}

	public void setPosition(float x, float y)
	{
		this.position.set(x, y);
	}

	public void setWidth(float width)
	{
		this.size.x = width;
	}

	public void setHeight(float height)
	{
		this.size.y = height;
	}

	public final Vector2fc getPosition()
	{
		return this.position;
	}

	public final Vector2fc getSize()
	{
		return this.size;
	}

	public static IntersectionData test(AxisAlignedBoundingBox collider, AxisAlignedBoundingBox other, IntersectionData dst)
	{
		final float ox = other.getPosition().x();
		final float oy = other.getPosition().y();
		final float ow = other.getSize().x() / 2F;
		final float oh = other.getSize().y() / 2F;

		final float cx = collider.getPosition().x();
		final float cy = collider.getPosition().y();
		final float cw = collider.getSize().x() / 2F;
		final float ch = collider.getSize().y() / 2F;

		float dx = ox - cx;
		float px = (ow + cw) - Math.abs(dx);
		if (px <= 0) return null;

		float dy = oy - cy;
		float py = (oh + ch) - Math.abs(dy);
		if (py <= 0) return null;

		if (px < py)
		{
			float sx = MathUtil.sign(dx);
			dst.delta.x = px * sx;
			dst.normal.x = sx;
			dst.point.x = cx + (cw * sx);
			dst.point.y = oy;

			dst.delta.y = 0;
			dst.normal.y= 0;
		}
		else
		{
			float sy = MathUtil.sign(dy);
			dst.delta.y = py * sy;
			dst.normal.y = sy;
			dst.point.x = ox;
			dst.point.y = cy + (ch * sy);

			dst.delta.x = 0;
			dst.normal.x = 0;
		}

		return dst;
	}

	public static IntersectionData test(AxisAlignedBoundingBox collider, float x, float y, float dx, float dy, float paddingX, float paddingY, IntersectionData dst)
	{
		final float cx = collider.getPosition().x();
		final float cy = collider.getPosition().y();
		final float cw = collider.getSize().x() / 2F;
		final float ch = collider.getSize().y() / 2F;

		float scaleX = 1.0F / dx;
		float scaleY = 1.0F / dy;
		float signX = MathUtil.sign(scaleX);
		float signY = MathUtil.sign(scaleY);

		float deltaTimeX = signX * (cw + paddingX);
		float deltaTimeY = signY * (ch + paddingY);

		float nearTimeX = (cx - deltaTimeX - x) * scaleX;
		float nearTimeY = (cy - deltaTimeY - y) * scaleY;
		float farTimeX = (cx + deltaTimeX - x) * scaleX;
		float farTimeY = (cy + deltaTimeY - y) * scaleY;

		if (nearTimeX > farTimeY || nearTimeY > farTimeY) return null;

		float nearTime = Math.max(nearTimeX, nearTimeY);
		float farTime = Math.min(farTimeX, farTimeY);

		if (nearTime >= 1 || farTime <= 0) return null;

		float dt = MathUtil.clamp(nearTime, 0, 1);
		if (nearTimeX > nearTimeY)
		{
			dst.normal.x = -signX;
			dst.normal.y = 0;
		}
		else
		{
			dst.normal.x = 0;
			dst.normal.y = -signY;
		}

		dst.delta.x = dt * dx;
		dst.delta.y = dt * dy;
		dst.point.x = x + dst.delta.x;
		dst.point.y = y + dst.delta.y;
		return dst;
	}

	public static IntersectionData testSweep(AxisAlignedBoundingBox collider, AxisAlignedBoundingBox other, float dx, float dy, IntersectionData dst)
	{
		if (dx == 0 && dy == 0)
		{
			return test(collider, other, dst);
		}
		else
		{
			return test(collider, other.getPosition().x(), other.getPosition().y(), dx, dy, other.getSize().x(), other.getSize().y(), dst);
		}
	}
}
