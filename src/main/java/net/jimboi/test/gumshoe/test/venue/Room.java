package net.jimboi.test.gumshoe.test.venue;

import net.jimboi.test.gumshoe.test.opportunity.inspection.ItemContainer;
import net.jimboi.test.gumshoe.test.venue.layout.Location;

/**
 * Created by Andy on 12/21/17.
 */
public class Room
{
	protected final Venue venue;
	protected final Location location;

	private ItemContainer container = new ItemContainer();

	public Room(Venue venue, Location location)
	{
		this.venue = venue;
		this.location = location;
	}

	public ItemContainer getContainer()
	{
		return this.container;
	}

	public final Venue getVenue()
	{
		return this.venue;
	}

	public final Location getLocation()
	{
		return this.location;
	}

	public String getName()
	{
		return this.location.id.toUpperCase();
	}
}
