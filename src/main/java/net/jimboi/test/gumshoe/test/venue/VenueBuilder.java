package net.jimboi.test.gumshoe.test.venue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 12/5/17.
 */
public class VenueBuilder
{
	private Map<String, RoomBuilder> rooms = new HashMap<>();
	private Set<EntranceBuilder> entrances = new HashSet<>();

	private RoomBuilder root;

	public VenueBuilder()
	{
		this.root = new RoomBuilder("outside");
		this.rooms.put(this.root.id, this.root);
	}

	public VenueLayout bake()
	{
		VenueLayout venue = new VenueLayout();

		for(Map.Entry<String, RoomBuilder> entry : this.rooms.entrySet())
		{
			String key = entry.getKey();
			RoomBuilder rb = entry.getValue();
			Room room = new Room(venue, rb.id);
			venue.rooms.put(key, room);
		}

		for(EntranceBuilder eb : this.entrances)
		{
			Entrance entrance = new Entrance(eb.type,
					venue.rooms.get(eb.room1),
					venue.rooms.get(eb.room2));
			venue.entrances.add(entrance);
		}

		return venue;
	}

	public RoomBuilder getRoot()
	{
		return this.root;
	}

	public RoomBuilder get(String roomName)
	{
		return this.rooms.get(roomName);
	}

	public class RoomBuilder
	{
		private final String id;

		RoomType type;
		String decorator;

		public RoomBuilder(String id)
		{
			this.id = id;
		}

		public RoomBuilder setType(RoomType type)
		{
			this.type = type;
			return this;
		}

		public RoomBuilder setDecorator(String decorator)
		{
			this.decorator = decorator;
			return this;
		}

		public void add(String roomID, EntranceType type)
		{
			RoomBuilder rb = new RoomBuilder(roomID);
			EntranceBuilder eb = new EntranceBuilder(this.id, rb.id);
			eb.setType(type);

			VenueBuilder.this.rooms.put(roomID, rb);
			VenueBuilder.this.entrances.add(eb);
		}
	}

	public class EntranceBuilder
	{
		private final String room1;
		private final String room2;

		EntranceType type;

		public EntranceBuilder(String room1, String room2)
		{
			this.room1 = room1;
			this.room2 = room2;
		}

		public EntranceBuilder setType(EntranceType type)
		{
			this.type = type;
			return this;
		}
	}
}
