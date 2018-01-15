package net.jimboi.test.tilemapper.suger.baron;

import org.joml.Vector2d;
import org.joml.Vector2f;

/**
 * Created by Andy on 11/9/17.
 */
public class ViewPort
{
	private static final double MAX_TILE_SIZE = 128;
	private static final double MIN_TILE_SIZE = 16;

	private double cursorX;
	private double cursorY;
	private boolean cursorDown;

	private double x;
	private double y;
	private double width;
	private double height;

	private double unitScale = 32;

	public void move(double dx, double dy)
	{
		this.x += dx;
		this.y += dy;
	}

	public void zoom(double ds)
	{
		this.unitScale *= ds;

		if (this.unitScale < MIN_TILE_SIZE) this.unitScale = MIN_TILE_SIZE;
		if (this.unitScale > MAX_TILE_SIZE) this.unitScale = MAX_TILE_SIZE;
	}

	public void setCursorPosition(double x, double y)
	{
		this.cursorX = x;
		this.cursorY = y;
	}

	public void setCursorDown(boolean down)
	{
		this.cursorDown = down;
	}

	public void setSize(double width, double height)
	{
		this.width = width;
		this.height = height;
	}

	public double getCursorX()
	{
		return this.cursorX;
	}

	public double getCursorY()
	{
		return this.cursorY;
	}

	public boolean isCursorDown()
	{
		return this.cursorDown;
	}

	public double getUnitScale()
	{
		return this.unitScale;
	}

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	public double getWidth()
	{
		return this.width;
	}

	public double getHeight()
	{
		return this.height;
	}

	public static Vector2d getScreenPos(ViewPort view, float posX, float posY, Vector2d dst)
	{
		return dst.set(posX * view.unitScale + view.x, posY * view.unitScale + view.y);
	}

	public static Vector2f getWorldPos(ViewPort view, double x, double y, Vector2f dst)
	{
		return dst.set((float) ((x - view.x) / view.unitScale),
				(float) ((y - view.y) / view.unitScale));
	}
}
