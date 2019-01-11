package canary.test.gumshoe.test.opportunity.exploration;

import canary.test.gumshoe.test.venue.layout.Location;
import canary.test.gumshoe.test.venue.layout.VenueLayout;

import java.util.Stack;

/**
 * Created by Andy on 12/6/17.
 */
public class Explorer
{
	private final VenueLayout venue;
	private final Stack<Location> path;

	public Explorer(VenueLayout venue)
	{
		this.venue = venue;
		this.path = new Stack<>();
		this.path.push(this.venue.getOutside());
	}

	public void moveTo(Location room)
	{
		this.path.push(room);
	}

	public void moveBack()
	{
		this.path.pop();
	}

	public Location getPreviousRoom()
	{
		return this.path.size() > 1 ? this.path.elementAt(this.path.size() - 2) : null;
	}

	public Location getCurrentRoom()
	{
		return this.path.isEmpty() ? null : this.path.peek();
	}

	public VenueLayout getVenue()
	{
		return this.venue;
	}
}
