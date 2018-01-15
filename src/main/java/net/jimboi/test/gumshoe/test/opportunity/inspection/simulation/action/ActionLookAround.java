package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.action;

import net.jimboi.test.gumshoe.test.opportunity.inspection.Actor;
import net.jimboi.test.gumshoe.test.opportunity.inspection.memory.Memory;
import net.jimboi.test.gumshoe.test.opportunity.inspection.memory.SensoryMemory;
import net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.Sensory;
import net.jimboi.test.gumshoe.test.venue.Venue;
import net.jimboi.test.sleuth.data.Time;

/**
 * Created by Andy on 12/20/17.
 */
public class ActionLookAround extends Action
{
	@Override
	public void execute(Venue venue, Time time, Actor actor)
	{
		for(Actor other : venue.getActors())
		{
			if (other.getLocation() == actor.getLocation())
			{
				if (other == actor)
				{
					actor.getDatabase().offer(new Memory(time, "was at the " + actor.getLocation()));
				}
				else
				{
					actor.getDatabase().offer(new SensoryMemory(time, Sensory.VISUAL, other, "at the " + actor.getLocation()));
				}
			}
		}
	}
}
