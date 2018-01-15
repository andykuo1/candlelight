package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.goapi;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 12/9/17.
 */
public class Test
{
	public static void main(String[] args)
	{
		final List<Action> actions = new LinkedList<>();
		final Environment goal = new Environment();
		final GOAP planner = new GOAP();

		goal.set("MakeFirewood", true);

		actions.add(new Action("CollectBranches", 10)
				.addEffect("MakeFirewood", true));
		actions.add(new Action("GetAxe", 1)
				.addCondition("KnowAxe", true)
				.addEffect("HasAxe", true));
		actions.add(new Action("LookAround", 1)
				.addEffect("KnowAxe", true));
		actions.add(new Action("ChopLog", 4)
				.addCondition("HasAxe", true)
				.addEffect("MakeFirewood", true));

		List<Action> plan = planner.plan(env-> actions, new Environment(), goal);
		for(Action action : plan)
		{
			System.out.println(action);
		}
		System.exit(0);
	}
}
