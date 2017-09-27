package net.jimboi.test.sleuth.comm;

import net.jimboi.test.sleuth.comm.environ.Action;
import net.jimboi.test.sleuth.comm.environ.ActionState;
import net.jimboi.test.sleuth.comm.environ.Environment;
import net.jimboi.test.sleuth.comm.environ.GoalState;

import org.bstone.util.astar.AStar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 9/27/17.
 */
public class AStarPlanner extends AStar<Environment, Integer>
{
	protected final Agent agent;

	public AStarPlanner(Agent agent)
	{
		super(
				(integer, integer2) -> integer + integer2,
				(actionState, actionState2) ->
				{
					Action action = ((ActionState) actionState2).getAction();
					if (action == null) return 0;
					return action.evaluate(agent, actionState);
				},
				(actionState, goal) -> ((GoalState) goal).getRemainingCostValue(actionState),
				actionState ->
				{
					List<Environment> states = new ArrayList<>();
					for(Action action : agent.getPossibleActions(actionState))
					{
						if (action.hasRequiredState(agent, actionState))
						{
							states.add(new ActionState(actionState, action));
						}
					}
					return states;
				},
				(actionState, goal) -> ((GoalState) goal).isSatisfied(actionState));

		this.agent = agent;
	}

	public Agent getAgent()
	{
		return this.agent;
	}
}
