package canary.zilar.dungeon;

import canary.zilar.dungeon.maze.RegionHandler;

import java.util.Random;

/**
 * Created by Andy on 5/29/17.
 */
public class RoomBuilder
{
	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public RoomBuilder(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public RoomData generate(RegionHandler regionHandler, Random rand, int x, int y, int minSize, int maxSize)
	{
		int size = (minSize + rand.nextInt(maxSize)) * 2 + 1;
		int rectable = rand.nextInt(1 + size / 2) * 2;

		int width = size;
		int height = size;

		if (rand.nextBoolean())
		{
			width += rectable;
		}
		else
		{
			height += rectable;
		}

		x += rand.nextInt((this.width - width) / 2) * 2 + 1;
		y += rand.nextInt((this.height - height) / 2) * 2 + 1;

		RoomData room = new RoomData(regionHandler.getNextAvailableRegion(), x, y, width - 1, height - 1);
		return room;
	}

	public RoomData generate(RegionHandler regionHandler, Random rand, int minSize, int maxSize)
	{
		int size = (minSize + rand.nextInt(maxSize)) * 2 + 1;
		int rectable = rand.nextInt(1 + size / 2) * 2;

		int width = size;
		int height = size;

		if (rand.nextBoolean())
		{
			width += rectable;
		}
		else
		{
			height += rectable;
		}

		int x = this.x + rand.nextInt((this.width - width) / 2) * 2 + 1;
		int y = this.y + rand.nextInt((this.height - height) / 2) * 2 + 1;

		RoomData room = new RoomData(regionHandler.getNextAvailableRegion(), x, y, width, height);
		return room;
	}
}
