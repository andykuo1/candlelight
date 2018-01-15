package net.jimboi.test.sleuth.example.story.venue;

import org.bstone.util.astar.AStar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Andy on 10/8/17.
 */
public final class VenueBuilder
{
	private String name = "Venue";

	private final Set<RoomBuilder> rbs = new HashSet<>();
	private final Set<EntranceBuilder> ebs = new HashSet<>();
	private int maxRoomID = 0;

	public VenueBuilder()
	{
	}

	public VenueBuilder setName(String name)
	{
		this.name = name;
		return this;
	}

	public RoomBuilder addRoom(int id)
	{
		RoomBuilder rb = new RoomBuilder(id);
		this.rbs.add(rb);
		if (this.maxRoomID < id) this.maxRoomID = id;
		return rb;
	}

	public EntranceBuilder addEntrance(int src, int dst)
	{
		EntranceBuilder eb = new EntranceBuilder(src, dst);
		this.ebs.add(eb);
		return eb;
	}

	public Venue bake()
	{
		Room[] rooms = new Room[this.maxRoomID + 1];
		Entrance[] entrances = new Entrance[this.ebs.size()];
		int i;

		for(RoomBuilder rb : this.rbs)
		{
			Room room = new Room();
			room.name = rb.name;
			rooms[rb.id] = room;
		}

		i = 0;
		for(EntranceBuilder eb : this.ebs)
		{
			Entrance entrance = new Entrance();
			entrance.src = rooms[eb.src];
			entrance.dst = rooms[eb.dst];
			entrances[i++] = entrance;
		}

		Venue venue = new Venue(this.name, rooms, entrances);

		AStar<Room, Integer> astar = new AStar<Room, Integer>()
		{
			@Override
			protected Integer getFScore(Integer value, Integer other)
			{
				return value + other;
			}

			@Override
			protected Integer getGScore(Room node, Room other)
			{
				return other.equals(node) ? 0 : 1;
			}

			@Override
			protected Integer getHScore(Room node, Room end)
			{
				return 1;
			}

			@Override
			protected Iterable<Room> getAvailableNodes(Room node)
			{
				List<Room> list = new ArrayList<>();
				if (node == null) return list;

				for(Entrance entrance : venue.getAvailableEntrances(node))
				{
					list.add(entrance.getDestination(node));
				}
				return list;
			}

			@Override
			protected boolean isEndNode(Room node, Room end)
			{
				return end.equals(node);
			}
		};

		Room outside = venue.getOutside();
		for(Room other : venue.getRooms())
		{
			if (astar.search(outside, other).isEmpty())
			{
				throw new IllegalArgumentException("has invalid layout - unreachable rooms");
			}
		}
		return venue;
	}

	public final class EntranceBuilder
	{
		public final int src;
		public final int dst;

		private EntranceBuilder(int src, int dst)
		{
			this.src = src;
			this.dst = dst;
		}

		public VenueBuilder pack()
		{
			return VenueBuilder.this;
		}

		@Override
		public boolean equals(Object o)
		{
			return o instanceof EntranceBuilder &&
					((this.src == ((EntranceBuilder) o).src && this.dst == ((EntranceBuilder) o).dst) ||
							(this.src == ((EntranceBuilder) o).dst && this.dst == ((EntranceBuilder) o).src));
		}
	}

	public final class RoomBuilder
	{
		public final int id;
		public String name;
		public List<String> items = new ArrayList<>();

		private RoomBuilder(int id)
		{
			this.id = id;
			this.name = "Room#" + id;
		}

		public RoomBuilder setName(String name)
		{
			this.name = name;
			return this;
		}

		public RoomBuilder addItem(String item)
		{
			this.items.add(item);
			return this;
		}

		public VenueBuilder pack()
		{
			return VenueBuilder.this;
		}

		@Override
		public boolean equals(Object o)
		{
			return o instanceof RoomBuilder && this.id == ((RoomBuilder) o).id;
		}
	}
}
