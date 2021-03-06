package canary.test.sleuth.base;

import canary.test.sleuth.cluedo.Room;

import canary.bstone.util.pair.UnorderedPair;

/**
 * Created by Andy on 10/7/17.
 */
public class Entrance
{
	private final UnorderedPair<Room> rooms;
	private final EntranceType type;

	public Entrance(EntranceType type, Room room1, Room room2)
	{
		this.type = type;
		this.rooms = new UnorderedPair<>(room1, room2);
	}

	public Room getOtherRoom(Room from)
	{
		return from == this.rooms.getFirst() || from != null && from.equals(this.rooms.getFirst()) ? this.rooms.getSecond() : this.rooms.getFirst();
	}

	public boolean hasRoom(Room room)
	{
		return this.rooms.contains(room);
	}

	public EntranceType getType()
	{
		return this.type;
	}

	@Override
	public String toString()
	{
		return this.type.toString() + ":" + this.rooms;
	}
}
