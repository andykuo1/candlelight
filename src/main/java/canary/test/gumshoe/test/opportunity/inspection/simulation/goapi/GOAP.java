package canary.test.gumshoe.test.opportunity.inspection.simulation.goapi;

import canary.bstone.util.astar.AStar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 12/9/17.
 */
public class GOAP
{
	private final Planner planner;

	public GOAP()
	{
		this.planner = new Planner();
	}

	public List<Action> plan(ActionableAgent agent, Environment initial, Environment goal)
	{
		List<Action> actions = new LinkedList<>();
		Iterator<Environment> iter = this.planner
				.setAgent(agent)
				.search(initial, goal).iterator();
		if (!iter.hasNext()) return actions;

		//Skip the start node
		iter.next();

		//Do the rest...
		while(iter.hasNext())
		{
			actions.add(((SnapshotEnvironment) iter.next()).getAction());
		}

		return actions;
	}

	private static final class Planner extends AStar<Environment, Integer>
	{
		private ActionableAgent agent;

		protected Planner setAgent(ActionableAgent agent)
		{
			this.agent = agent;
			return this;
		}

		@Override
		protected Integer getFScore(Integer value, Integer other)
		{
			return value + other;
		}

		@Override
		protected Integer getGScore(Environment node, Environment other)
		{
			if (other instanceof SnapshotEnvironment)
			{
				Action action = ((SnapshotEnvironment) other).getAction();
				if (action == null) return 0;
				return action.evaluateCost(node, this.agent);
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
			SnapshotEnvironment snapshot = null;

			for(Action action : this.agent.getAvailableAction(node))
			{
				if (action.isConditionSatisfied(node))
				{
					if (snapshot == null) snapshot = new SnapshotEnvironment(node);
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
	}
}
