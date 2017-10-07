package net.jimboi.test.sleuth.base;

import net.jimboi.test.sleuth.base.builder.Builder;
import net.jimboi.test.sleuth.cluedo.Room;
import net.jimboi.test.sleuth.cluedo.RoomType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 10/7/17.
 */
public class VenueBuilder extends Builder
{
	protected final Map<Integer, VenueRoomBuilder> rooms = new HashMap<>();
	protected final Set<VenueEntranceBuilder> entrances = new HashSet<>();

	public VenueRoomBuilder createRoom(RoomType type, int id)
	{
		return new VenueRoomBuilder(this, id)
				.setRoomType(type);
	}

	public VenueEntranceBuilder createEntrance(EntranceType type, int from, int to)
	{
		return new VenueEntranceBuilder(this, from, to)
				.setEntranceType(type);
	}

	public VenueBuilder createEntryPoint(int id)
	{
		return new VenueEntranceBuilder(this, -1, id)
				.setEntranceType(EntranceType.OUTSIDE)
				.pack();
	}

	@Override
	public VenueLayout bake()
	{
		Map<Integer, Room> roomMapping = new HashMap<>();
		Set<Entrance> entrances = new HashSet<>();

		for(VenueRoomBuilder rb : this.rooms.values())
		{
			roomMapping.put(rb.id, new Room(rb.type));
		}
		roomMapping.put(-1, null);

		for(VenueEntranceBuilder eb : this.entrances)
		{
			entrances.add(new Entrance(eb.type,
					roomMapping.get(eb.rooms.getFirst()),
					roomMapping.get(eb.rooms.getSecond())));
		}

		Set<Room> rooms = new HashSet<>();
		rooms.addAll(roomMapping.values());

		return new VenueLayout(rooms, entrances);
	}

	@Override
	public void clear()
	{
		this.rooms.clear();
		this.entrances.clear();
	}
}
