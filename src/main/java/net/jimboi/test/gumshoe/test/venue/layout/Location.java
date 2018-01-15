package net.jimboi.test.gumshoe.test.venue.layout;

import java.util.Collection;

/**
 * Created by Andy on 9/23/17.
 */
public class Location
{
	private final VenueLayout venue;
	public final String id;

	Location(VenueLayout venue, String id)
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
