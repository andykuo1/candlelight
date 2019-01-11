package canary.test.sleuth.comm.goap;

import canary.bstone.util.astar.AStar;

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
		this.actions = actions;
	}

	@Override
	protected Integer getFScore(Integer value, Integer other)
	{
		return value + other;
	}

	@Override
	protected Integer getGScore(Environment node, Environment other)
	{
		if (other instanceof ActionSnapshot)
		{
			Action action = ((ActionSnapshot) other).getAction();
			if (action == null) return 0;
			return action.evaluateCosts(node);
		}
		else
		{
			return node.equals(other) ? 0 : 1;
		}
	}

	@Override
	protected Integer getHScore(Environment node, Environment end)
	{
		return end.getEstimatedRemainingActionCost(node);
	}

	@Override
	protected Iterable<Environment> getAvailableNodes(Environment node)
	{
		List<Environment> states = new ArrayList<>();
		ActionSnapshot snapshot = null;

		for(Action action : actions.apply(node))
		{
			if (action.isSupportedConditions(node))
			{
				if (snapshot == null) snapshot = new ActionSnapshot(node);
				if (snapshot.process(action))
				{
					states.add(snapshot);
					snapshot = null;
				}
			}
		}
		return states;
	}

	@Override
	protected boolean isEndNode(Environment node, Environment end)
	{
		return end.isSatisfiedActionState(node);
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
