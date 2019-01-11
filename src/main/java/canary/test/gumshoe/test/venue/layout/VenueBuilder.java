package canary.test.gumshoe.test.venue.layout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 12/5/17.
 */
public class VenueBuilder
{
	private Map<String, LocationBuilder> locations = new HashMap<>();
	private Set<EntranceBuilder> entrances = new HashSet<>();

	private LocationBuilder root;

	public VenueBuilder()
	{
		this.root = new LocationBuilder("outside");
		this.locations.put(this.root.id, this.root);
	}

	public VenueLayout bake()
	{
		VenueLayout venue = new VenueLayout();

		for(Map.Entry<String, LocationBuilder> entry : this.locations.entrySet())
		{
			String key = entry.getKey();
			LocationBuilder lb = entry.getValue();
			Location location = new Location(venue, lb.id);
			venue.locations.put(key, location);
		}

		for(EntranceBuilder eb : this.entrances)
		{
			Entrance entrance = new Entrance(eb.type,
					venue.locations.get(eb.loc1),
					venue.locations.get(eb.loc2));
			venue.entrances.add(entrance);
		}

		return venue;
	}

	public LocationBuilder getRoot()
	{
		return this.root;
	}

	public LocationBuilder get(String locID)
	{
		return this.locations.get(locID);
	}

	public class LocationBuilder
	{
		private final String id;

		String decorator;

		public LocationBuilder(String id)
		{
			this.id = id;
		}

		public LocationBuilder setDecorator(String decorator)
		{
			this.decorator = decorator;
			return this;
		}

		public void add(String locID, EntranceType type)
		{
			LocationBuilder rb = new LocationBuilder(locID);
			EntranceBuilder eb = new EntranceBuilder(this.id, rb.id);
			eb.setType(type);

			VenueBuilder.this.locations.put(locID, rb);
			VenueBuilder.this.entrances.add(eb);
		}
	}

	public class EntranceBuilder
	{
		private final String loc1;
		private final String loc2;

		EntranceType type;

		public EntranceBuilder(String loc1, String loc2)
		{
			this.loc1 = loc1;
			this.loc2 = loc2;
		}

		public EntranceBuilder setType(EntranceType type)
		{
			this.type = type;
			return this;
		}
	}
}
