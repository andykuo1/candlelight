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
public class ActionConversation extends ActionObservable
{
	protected final Actor other;

	public ActionConversation(Actor other)
	{
		this.other = other;
	}

	@Override
	protected void observe(Venue venue, Time time, Actor actor, Actor observer)
	{
		Memory m;
		if (actor == observer)
		{
			m = new Memory(time, "was talking to " + this.other.getName() + ".");
		}
		else if (this.other == observer)
		{
			m = new Memory(time, "was talking to " + actor.getName() + ".");
		}
		else
		{
			m = new SensoryMemory(time, Sensory.VISUAL, actor, " was talking to " + this.other.getName() + ".");
		}
		observer.getDatabase().offer(m);
	}
}
