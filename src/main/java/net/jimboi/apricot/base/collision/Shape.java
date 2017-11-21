package net.jimboi.apricot.base.collision;

import org.joml.Vector2f;

/**
 * Created by Andy on 7/20/17.
 */
public class Shape
{
	private float centerX;
	private float centerY;

	public Shape(float x, float y)
	{
		this.setCenter(x, y);
	}

	public float getCenterX()
	{
		return this.centerX;
	}

	public float getCenterY()
	{
		return this.centerY;
	}

	public Vector2f getCenter(Vector2f dst)
	{
		return dst.set(this.getCenterX(), this.getCenterY());
	}

	public void setCenter(float x, float y)
	{
		this.centerX = x;
		this.centerY = y;
	}

	public static final class AABB extends Shape
	{
		private float halfWidth;
		private float halfHeight;

		public AABB(float x, float y, float width, float height)
		{
			super(x, y);

			this.setHalfSize(width / 2F, height / 2F);
		}

		public AABB(float x, float y, float halfSize)
		{
			super(x, y);

			this.setHalfSize(halfSize, halfSize);
		}

		public float getHalfWidth()
		{
			return this.halfWidth;
		}

		public float getHalfHeight()
		{
			return this.halfHeight;
		}

		public Vector2f getHalfSize(Vector2f dst)
		{
			return dst.set(this.getHalfWidth(), this.getHalfHeight());
		}

		public void setHalfSize(float halfWidth, float halfHeight)
		{
			this.halfWidth = halfWidth;
			this.halfHeight = halfHeight;
		}
	}

	public static final class Segment extends Shape
	{
		private float deltaX;
		private float deltaY;

		public Segment(float x, float y, float dx, float dy)
		{
			super(x, y);

			this.setDelta(dx, dy);
		}

		public float getDeltaX()
		{
			return this.deltaX;
		}

		public float getDeltaY()
		{
			return this.deltaY;
		}

		public Vector2f getDelta(Vector2f dst)
		{
			return dst.set(this.getDeltaX(), this.getDeltaY());
		}

		public void setDelta(float x, float y)
		{
			this.deltaX = x;
			this.deltaY = y;
		}
	}

	public static final class Circle extends Shape
	{
		private float radius;

		public Circle(float x, float y, float r)
		{
			super(x, y);

			this.setRadius(r);
		}

		public float getRadius()
		{
			return this.radius;
		}

		public void setRadius(float radius)
		{
			this.radius = radius;
		}
	}

	public static final class Point extends Shape
	{
		public Point(float x, float y)
		{
			super(x, y);
		}
	}
}
