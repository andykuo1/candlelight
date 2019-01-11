package canary.test.gumshoe.test.venue;

import canary.test.gumshoe.test.venue.layout.Location;
import canary.test.gumshoe.test.venue.layout.VenueLayout;

import canary.bstone.util.astar.AStar;

import java.util.ArrayList;

/**
 * Created by Andy on 12/21/17.
 */
public class VenueNavigator extends AStar<Location, Integer>
{
	protected final VenueLayout layout;

	public VenueNavigator(VenueLayout layout)
	{
		this.layout = layout;
	}

	@Override
	protected Integer getFScore(Integer value, Integer other)
	{
		return value + other;
	}

	@Override
	protected Integer getGScore(Location node, Location other)
	{
		return !node.equals(other) ? 1 : 0;
	}

	@Override
	protected Integer getHScore(Location node, Location end)
	{
		return 1;
	}

	@Override
	protected Iterable<Location> getAvailableNodes(Location node)
	{
		return this.layout.getAdjacentLocations(node, new ArrayList<>());
	}

	@Override
	protected boolean isEndNode(Location node, Location end)
	{
		return end.equals(node);
	}

	public final VenueLayout getLayout()
	{
		return this.layout;
	}
}
