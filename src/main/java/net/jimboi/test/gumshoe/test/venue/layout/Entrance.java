package net.jimboi.test.gumshoe.test.venue.layout;

import org.bstone.util.pair.UnorderedPair;

/**
 * Created by Andy on 10/7/17.
 */
public class Entrance
{
	private final UnorderedPair<Location> locations;
	private final EntranceType type;

	Entrance(EntranceType type, Location loc1, Location loc2)
	{
		this.type = type;
		this.locations = new UnorderedPair<>(loc1, loc2);
	}

	public Location getOtherLocation(Location from)
	{
		return from == this.locations.getFirst() || from != null && from.equals(this.locations.getFirst()) ? this.locations.getSecond() : this.locations.getFirst();
	}

	public boolean hasLocation(Location loc)
	{
		return this.locations.contains(loc);
	}

	public EntranceType getType()
	{
		return this.type;
	}

	@Override
	public String toString()
	{
		return this.type.toString() + ":" + this.locations;
	}
}
