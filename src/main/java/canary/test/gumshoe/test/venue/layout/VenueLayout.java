package canary.test.gumshoe.test.venue.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 10/7/17.
 */
public class VenueLayout
{
	final Map<String, Location> locations = new HashMap<>();
	final List<Entrance> entrances = new ArrayList<>();

	VenueLayout()
	{
	}

	public <T extends Collection<Entrance>> T getAvailableEntrances(Location from, Location to, T dst)
	{
		for(Entrance entrance : this.entrances)
		{
			if (entrance.hasLocation(from) && entrance.hasLocation(to))
			{
				dst.add(entrance);
			}
		}
		return dst;
	}

	public <T extends Collection<Entrance>> T getAvailableEntrances(Location from, T dst)
	{
		for(Entrance entrance : this.entrances)
		{
			if (entrance.hasLocation(from))
			{
				dst.add(entrance);
			}
		}
		return dst;
	}

	public <T extends Collection<Location>> T getAdjacentLocations(Location from, T dst)
	{
		for(Entrance entrance : this.entrances)
		{
			if (entrance.hasLocation(from))
			{
				dst.add(entrance.getOtherLocation(from));
			}
		}
		return dst;
	}

	public boolean isLocation(Location location)
	{
		if (location == null) return false;
		return this.locations.containsValue(location);
	}

	public Location getLocation(String roomID)
	{
		return this.locations.get(roomID);
	}

	public Location getOutside()
	{
		return this.locations.get("outside");
	}

	public Iterable<Location> getLocations()
	{
		return this.locations.values();
	}

	public Iterable<Entrance> getEntrances()
	{
		return this.entrances;
	}

	public int getLocationCount()
	{
		return this.locations.size();
	}
}
