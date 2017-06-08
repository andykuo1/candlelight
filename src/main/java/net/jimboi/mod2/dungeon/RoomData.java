package net.jimboi.mod2.dungeon;

import net.jimboi.mod2.dungeon.maze.Region;

import org.bstone.util.map2d.IntMap;

/**
 * Created by Andy on 5/28/17.
 */
public class RoomData implements Region
{
	private int id;
	private IntMap map;

	public int x;
	public int y;

	public int width;
	public int height;

	public RoomData(int id, int x, int y, int width, int height)
	{
		this.id = id;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.map = new IntMap(this.width, this.height);
	}

	public IntMap getTiles()
	{
		return this.map;
	}

	@Override
	public int getRegionID()
	{
		return this.id;
	}

	public boolean intersects(RoomData roomData)
	{
		return this.x <= roomData.x + roomData.width &&
				this.x + this.width >= roomData.x &&
				this.y <= roomData.y + roomData.height &&
				this.y + this.height >= roomData.y;
	}

	public String toString()
	{
		return this.id + " : " + this.x + ", " + this.y + " : " + this.width + " x " + this.height;
	}
}
