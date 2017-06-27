package org.zilar.dungeon.maze;

/**
 * Created by Andy on 5/29/17.
 */
public class RegionHandler
{
	private int region = 1;

	public int getNextAvailableRegion()
	{
		return this.region++;
	}

	public int size()
	{
		return this.region;
	}
}
