package canary.test.gumshoe.test.opportunity.inspection.simulation.action;

import canary.test.gumshoe.test.opportunity.inspection.Actor;
import canary.test.gumshoe.test.opportunity.inspection.memory.Memory;
import canary.test.gumshoe.test.opportunity.inspection.memory.SensoryMemory;
import canary.test.gumshoe.test.opportunity.inspection.simulation.Sensory;
import canary.test.gumshoe.test.venue.Venue;
import canary.test.sleuth.data.Time;

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
