package canary.test.gumshoe.test.opportunity.inspection;

import canary.test.gumshoe.test.venue.Venue;
import canary.test.gumshoe.test.venue.layout.VenueLayout;

/**
 * Created by Andy on 12/6/17.
 */
public class Inspector
{
	private final Venue venue;

	public Inspector(VenueLayout layout)
	{
		this.venue = new Venue(layout);
	}

	public Venue getVenue()
	{
		return this.venue;
	}
}
