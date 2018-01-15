package net.jimboi.test.gumshoe.test.venue;

import net.jimboi.test.gumshoe.test.opportunity.inspection.Actor;
import net.jimboi.test.gumshoe.test.venue.layout.Location;
import net.jimboi.test.gumshoe.test.venue.layout.VenueLayout;

import org.qsilver.util.iterator.FilterIterator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 12/6/17.
 */
public class Venue
{
	private final VenueLayout layout;
	private final VenueNavigator navigator;
	private Set<Actor> actors = new HashSet<>();
	private Map<Location, Room> rooms = new HashMap<>();

	public Venue(VenueLayout layout)
	{
		this.layout = layout;
		this.navigator = new VenueNavigator(this.layout);
	}

	public void addActor(Actor actor)
	{
		this.actors.add(actor);
		actor.setVenue(this);
		if (!this.layout.isLocation(actor.getLocation()))
		{
			actor.setLocation(this.layout.getOutside());
		}
	}

	public void removeActor(Actor actor)
	{
		this.actors.remove(actor);
		actor.setVenue(null);
		actor.setLocation(null);
	}

	public Room getRoom(Location location)
	{
		return this.rooms.computeIfAbsent(location, location1 -> new Room(this, location1));
	}

	public Iterable<Actor> getActors()
	{
		return this.actors;
	}

	public Iterable<Actor> getActorsByLocation(Location location)
	{
		return () -> new FilterIterator<>(this.actors.iterator(), actor -> location.equals(actor.getLocation()));
	}

	public VenueNavigator getNavigator()
	{
		return this.navigator;
	}

	public VenueLayout getLayout()
	{
		return this.layout;
	}
}
