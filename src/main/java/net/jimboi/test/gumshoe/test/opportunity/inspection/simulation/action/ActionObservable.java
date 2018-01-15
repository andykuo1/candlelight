package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.action;

import net.jimboi.test.gumshoe.test.opportunity.inspection.Actor;
import net.jimboi.test.gumshoe.test.venue.Venue;
import net.jimboi.test.sleuth.data.Time;

/**
 * Created by Andy on 12/20/17.
 */
public abstract class ActionObservable extends Action
{
	@Override
	public void execute(Venue venue, Time time, Actor actor)
	{
		for(Actor other : venue.getActors())
		{
			if (this.isObservable(venue, time, actor, other))
			{
				this.observe(venue, time, actor, other);
			}
		}
	}

	protected abstract void observe(Venue venue, Time time, Actor actor, Actor observer);

	protected boolean isObservable(Venue venue, Time time, Actor actor, Actor observer)
	{
		return observer.getLocation() == actor.getLocation();
	}
}
