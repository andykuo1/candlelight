package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.action;

import net.jimboi.test.gumshoe.test.opportunity.inspection.Actor;
import net.jimboi.test.gumshoe.test.venue.Venue;
import net.jimboi.test.sleuth.data.Time;

/**
 * Created by Andy on 12/20/17.
 */
public class ActionSequence extends Action
{
	protected final Action[] actions;

	public ActionSequence(Action... actions)
	{
		this.actions = actions;
	}

	@Override
	public void execute(Venue venue, Time time, Actor actor)
	{
		for(int i = 0; i < this.actions.length; ++i)
		{
			if (this.onExecute(venue, time, actor, i))
			{
				this.actions[i].execute(venue, time, actor);
			}
		}
	}

	protected boolean onExecute(Venue venue, Time time, Actor actor, int actionIndex)
	{
		return true;
	}

	public final Action[] getActions()
	{
		return this.actions;
	}
}
