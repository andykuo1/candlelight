package net.jimboi.mod.worldgen;

/**
 * Created by Andy on 5/12/17.
 */
public final class Room
{
	public int x;
	public int y;
	public int width;
	public int height;

	public void offset(int x, int y)
	{
		this.x += x;
		this.y += y;
	}

	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public Room centerAt(int x, int y, Room dst)
	{
		int w2 = this.width / 2;
		int h2 = this.height / 2;

		dst.x = x - w2;
		dst.y = y - h2;
		dst.width = this.width;
		dst.height = this.height;

		return dst;
	}

	public Room inflate(int amount, Room dst)
	{
		dst.x -= amount;
		dst.y -= amount;
		int amount2 = amount * 2;
		this.width += amount2;
		this.height += amount2;

		return dst;
	}

	public boolean contains(int x, int y)
	{
		return x >= this.x &&
				x <= this.x + this.width &&
				y >= this.y &&
				y <= this.y + this.height;
	}

	public boolean intersects(Room room)
	{
		return this.x < room.x + room.width &&
				this.x + this.width > room.x &&
				this.y < room.y + room.height &&
				this.y + this.height > room.y;
	}
}
