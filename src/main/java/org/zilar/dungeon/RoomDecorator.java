package org.zilar.dungeon;

import net.jimboi.boron.base_ab.gridmap.IntMap;

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
		map.clear(1);

		for (int x = 0; x < map.getWidth(); ++x)
		{
			for (int y = 0; y < map.getHeight(); ++y)
			{
				if (this.rand.nextFloat() < 0.3F)
				{
					map.put(x, y, 8);
				}
			}
		}

		return roomData;
	}
}
