package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.action;

import net.jimboi.test.gumshoe.test.opportunity.inspection.Actor;
import net.jimboi.test.gumshoe.test.opportunity.inspection.memory.Memory;
import net.jimboi.test.gumshoe.test.opportunity.inspection.memory.SensoryMemory;
import net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.Sensory;
import net.jimboi.test.gumshoe.test.venue.Venue;
import net.jimboi.test.gumshoe.test.venue.layout.Entrance;
import net.jimboi.test.gumshoe.test.venue.layout.Location;
import net.jimboi.test.sleuth.data.Time;

/**
 * Created by Andy on 12/20/17.
 */
public class ActionLocationGoto extends ActionSequence
{
	public ActionLocationGoto(Entrance entrance)
	{
		super(new SubActionLocationExit(),
				new SubActionLocationEnter(entrance),
				new ActionLookAround());
	}

	public final Entrance getEntrance()
	{
		return ((SubActionLocationEnter) this.actions[1]).entrance;
	}

	public static class SubActionLocationEnter extends ActionObservable
	{
		protected final Entrance entrance;

		SubActionLocationEnter(Entrance entrance)
		{
			this.entrance = entrance;
		}

		@Override
		public void execute(Venue venue, Time time, Actor actor)
		{
			Location dst = this.entrance.getOtherLocation(actor.getLocation());
			actor.setLocation(dst);

			super.execute(venue, time, actor);
		}

		@Override
		protected void observe(Venue venue, Time time, Actor actor, Actor observer)
		{
			Memory m;
			if (actor == observer)
			{
				m = new Memory(time, "was entering the " + actor.getLocation() + ".");
			}
			else
			{
				m = new SensoryMemory(time, Sensory.VISUAL, actor, "was entering the " + actor.getLocation() + ".");
			}
			observer.getDatabase().offer(m);
		}
	}

	public static class SubActionLocationExit extends ActionObservable
	{
		@Override
		protected void observe(Venue venue, Time time, Actor actor, Actor observer)
		{
			Memory m;
			if (actor == observer)
			{
				m = new Memory(time, "was leaving the " + actor.getLocation() + ".");
			}
			else
			{
				m = new SensoryMemory(time, Sensory.VISUAL, actor, "was leaving the " + actor.getLocation() + ".");
			}
			observer.getDatabase().offer(m);
		}
	}
}
