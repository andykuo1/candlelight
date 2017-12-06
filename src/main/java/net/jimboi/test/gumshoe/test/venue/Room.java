package net.jimboi.test.gumshoe.test.venue;

import java.util.Collection;

/**
 * Created by Andy on 9/23/17.
 */
public class Room
{
	private final VenueLayout venue;
	public final String id;

	Room(VenueLayout venue, String id)
	{
		this.venue = venue;
		this.id = id;
	}

	public Collection<Entrance> getEntrances(Collection<Entrance> dst)
	{
		return this.venue.getAvailableEntrances(this, dst);
	}

	public VenueLayout getVenue()
	{
		return this.venue;
	}

	@Override
	public String toString()
	{
		return "room:" + this.id;
	}
}
