package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.action;

import net.jimboi.test.gumshoe.test.opportunity.inspection.Actor;
import net.jimboi.test.gumshoe.test.venue.Venue;
import net.jimboi.test.sleuth.data.Time;

/**
 * Created by Andy on 12/20/17.
 */
public abstract class Action
{
	public abstract void execute(Venue venue, Time time, Actor actor);
}
