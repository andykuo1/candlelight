package net.jimboi.test.sleuth.comm.goap;

import org.bstone.util.astar.AStar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Andy on 9/27/17.
 */
public class GOAPPlanner extends AStar<Environment, Integer>
{
	protected final Function<Environment, Iterable<Action>> actions;

	public GOAPPlanner(Function<Environment, Iterable<Action>> actions)
	{
		super(
				(integer, integer2) -> integer + integer2,
				(state, state2) ->
				{
					if (state2 instanceof ActionSnapshot)
					{
						Action action = ((ActionSnapshot) state2).getAction();
						if (action == null) return 0;
						return action.evaluateCosts(state);
					}
					else
					{
						return state.equals(state2) ? 0 : 1;
					}
				},
				(state, goal) -> goal.getEstimatedRemainingActionCost(state),
				state ->
				{
					List<Environment> states = new ArrayList<>();
					ActionSnapshot snapshot = null;

					for(Action action : actions.apply(state))
					{
						if (action.isSupportedConditions(state))
						{
							if (snapshot == null) snapshot = new ActionSnapshot(state);
							if (snapshot.process(action))
							{
								states.add(snapshot);
								snapshot = null;
							}
						}
					}
					return states;
				},
				(state, goal) -> goal.isSatisfiedActionState(state));

		this.actions = actions;
	}

	public List<Action> plan(Environment initial, Environment goal)
	{
		List<Action> actions = new ArrayList<>();
		Iterator<Environment> iter = this.search(initial, goal).iterator();
		if (!iter.hasNext()) return actions;

		//Skip the start node
		iter.next();

		//Do the rest...
		while(iter.hasNext())
		{
			actions.add(((ActionSnapshot) iter.next()).getAction());
		}

		return actions;
	}
}
