package net.jimboi.test.sleuth.base;

import net.jimboi.test.sleuth.cluedo.Room;

import java.util.List;

/**
 * Created by Andy on 10/7/17.
 */
public class VenueLayout
{
	private final Iterable<Entrance> entrances;
	private final Iterable<Room> rooms;

	public VenueLayout(Iterable<Room> rooms, Iterable<Entrance> entrances)
	{
		this.entrances = entrances;
		this.rooms = rooms;
	}

	public List<Entrance> getAvailableEntrances(Room from, List<Entrance> dst)
	{
		for(Entrance entrance : this.entrances)
		{
			if (entrance.hasRoom(from))
			{
				dst.add(entrance);
			}
		}

		return dst;
	}

	public Iterable<Entrance> getEntrances()
	{
		return this.entrances;
	}

	public Iterable<Room> getRooms()
	{
		return this.rooms;
	}
}
