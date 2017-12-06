package net.jimboi.test.gumshoe.test.exploration;

import net.jimboi.test.gumshoe.test.venue.Room;
import net.jimboi.test.gumshoe.test.venue.VenueLayout;

import java.util.Stack;

/**
 * Created by Andy on 12/6/17.
 */
public class Explorer
{
	private final VenueLayout venue;
	private final Stack<Room> path;

	public Explorer(VenueLayout venue)
	{
		this.venue = venue;
		this.path = new Stack<>();
		this.path.push(this.venue.getOutside());
	}

	public void moveTo(Room room)
	{
		this.path.push(room);
	}

	public void moveBack()
	{
		this.path.pop();
	}

	public boolean canMoveBack()
	{
		return this.path.size() > 1;
	}

	public Room getCurrentRoom()
	{
		return this.path.isEmpty() ? null : this.path.peek();
	}

	public VenueLayout getVenue()
	{
		return this.venue;
	}
}
