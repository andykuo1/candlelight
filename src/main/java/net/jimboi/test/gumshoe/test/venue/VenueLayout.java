package net.jimboi.test.gumshoe.test.venue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 10/7/17.
 */
public class VenueLayout
{
	final Map<String, Room> rooms = new HashMap<>();
	final List<Entrance> entrances = new ArrayList<>();

	VenueLayout()
	{
	}

	public Collection<Entrance> getAvailableEntrances(Room from, Room to, Collection<Entrance> dst)
	{
		for(Entrance entrance : this.entrances)
		{
			if (entrance.hasRoom(from) && entrance.hasRoom(to))
			{
				dst.add(entrance);
			}
		}
		return dst;
	}

	public Collection<Entrance> getAvailableEntrances(Room from, Collection<Entrance> dst)
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

	public Room getRoom(String roomID)
	{
		return this.rooms.get(roomID);
	}

	public Room getOutside()
	{
		return this.rooms.get("outside");
	}

	public Iterable<Room> getRooms()
	{
		return this.rooms.values();
	}

	public Iterable<Entrance> getEntrances()
	{
		return this.entrances;
	}
}
