package net.jimboi.test.sleuth.comm;

import net.jimboi.test.sleuth.comm.environ.Action;
import net.jimboi.test.sleuth.comm.environ.ActionState;
import net.jimboi.test.sleuth.comm.environ.Environment;
import net.jimboi.test.sleuth.comm.environ.GoalState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 9/27/17.
 */
public class ThinkMachine
{
	private final AStarPlanner planner;
	private final Agent agent;

	public ThinkMachine(Agent agent)
	{
		this.agent = agent;
		this.planner = new AStarPlanner(this.agent);
	}

	public Iterable<Action> plan(Environment env, GoalState goal)
	{
		ActionState start = new ActionState(env, null);
		List<Action> actions = new ArrayList<>();
		Iterable<Environment> path = this.planner.search(start, goal);
		if (path == null) return actions;

		Iterator<Environment> iter = path.iterator();

		//Skip the first node...
		iter.next();

		//Get all the rest!
		for(Environment state : path)
		{
			Action action = ((ActionState) state).getAction();
			if (action == null) continue;
			actions.add(action);
		}
		return actions;
	}

	public Agent getAgent()
	{
		return this.agent;
	}
}
