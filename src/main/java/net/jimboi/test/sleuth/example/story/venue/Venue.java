package net.jimboi.test.sleuth.example.story.venue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andy on 10/8/17.
 */
public final class Venue
{
	private final String name;
	private final Room[] rooms;
	private final Entrance[] entrances;

	public Venue(String name, Room[] rooms, Entrance[] entrances)
	{
		this.name = name;
		this.rooms = rooms;
		this.entrances = entrances;
	}

	public List<Entrance> getAvailableEntrances(Room src)
	{
		List<Entrance> result = new ArrayList<>();
		for(Entrance entrance : this.entrances)
		{
			if (entrance.isDestination(src))
			{
				result.add(entrance);
			}
		}
		return result;
	}

	public Room getRoomByIndex(int index)
	{
		return this.rooms[index];
	}

	public Room getOutside()
	{
		return this.rooms[0];
	}

	public Iterable<Room> getRooms()
	{
		return Arrays.asList(this.rooms);
	}

	public Iterable<Entrance> getEntrances()
	{
		return Arrays.asList(this.entrances);
	}

	public String getName()
	{
		return this.name;
	}

	public int size()
	{
		return this.rooms.length;
	}

	@Override
	public String toString()
	{
		return this.name + ":" + Arrays.asList(this.rooms);
	}
}
