package canary.test.gumshoe.test.opportunity.inspection;

import canary.test.gumshoe.test.opportunity.inspection.memory.MemoryDatabase;
import canary.test.gumshoe.test.opportunity.inspection.simulation.Agent;
import canary.test.gumshoe.test.venue.Venue;
import canary.test.gumshoe.test.venue.layout.Location;

/**
 * Created by Andy on 12/6/17.
 */
public class Actor extends Agent
{
	private final String name;
	private Venue venue = null;
	private Location location = null;
	private MemoryDatabase database = new MemoryDatabase();
	private ItemContainer container = new ItemContainer();

	public Actor(String name)
	{
		this.name = name;
	}

	public Actor setVenue(Venue venue)
	{
		this.venue = venue;
		return this;
	}

	public Actor setLocation(Location room)
	{
		this.location = room;
		return this;
	}

	public MemoryDatabase getDatabase()
	{
		return this.database;
	}

	public ItemContainer getContainer()
	{
		return this.container;
	}

	public Location getLocation()
	{
		return this.location;
	}

	public Venue getVenue()
	{
		return this.venue;
	}

	public String getName()
	{
		return this.name;
	}
}
