package net.jimboi.stage_b.gnome.dungeon;

import org.qsilver.util.map2d.IntMap;

import java.util.Random;

/**
 * Created by Andy on 6/11/17.
 */
public class RoomDecorator
{
	private Random rand = new Random();

	public RoomDecorator()
	{
	}

	public RoomData decorate(RoomData roomData)
	{
		IntMap map = roomData.getTiles();
		IntMap.fill(map, 0, 0, map.width, map.height, 1);

		for (int x = 0; x < map.width; ++x)
		{
			for (int y = 0; y < map.height; ++y)
			{
				if (this.rand.nextFloat() < 0.3F)
				{
					map.set(x, y, 'a' + rand.nextInt('z'));
				}
			}
		}

		return roomData;
	}
}
