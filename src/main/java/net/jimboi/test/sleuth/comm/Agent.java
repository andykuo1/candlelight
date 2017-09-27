package net.jimboi.test.sleuth.comm;

import net.jimboi.test.sleuth.comm.environ.Action;
import net.jimboi.test.sleuth.comm.environ.ActionBase;
import net.jimboi.test.sleuth.comm.environ.Bias;
import net.jimboi.test.sleuth.comm.environ.BiasBase;
import net.jimboi.test.sleuth.comm.environ.Environment;
import net.jimboi.test.sleuth.comm.environ.Goal;
import net.jimboi.test.sleuth.comm.environ.GoalBase;
import net.jimboi.test.sleuth.comm.environ.GoalState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 9/27/17.
 */
public class Agent
{
	public final ThinkMachine thinker;
	public List<Goal> goals;
	public List<Action> actions;
	public List<Bias> biases;

	public Agent()
	{
		this.thinker = new ThinkMachine(this);

		//Generate goals
		goals = new ArrayList<>();
		goals.add(new GoalBase()
				.addGoal("MakeFirewood", true)
				.addGoal("CanMove", true)
				.addGoal("MakeTorch", true));

		//Generate actions
		actions = new ArrayList<>();

		actions.add(new ActionBase("CollectBranches", 8)
				.addRequirement("CanMove", true)
				.addEffect("CanMove", false, 0.4F)
				.addEffect("MakeFirewood", true));

		actions.add(new ActionBase("FindAxe", 2)
				.addRequirement("CanMove", true)
				.addRequirement("KnowLocation", true)
				.addEffect("FoundAxe", true));

		actions.add(new ActionBase("LookAround", 1)
				.addEffect("KnowLocation", true));

		actions.add(new ActionBase("GetAxe", 1)
				.addRequirement("FoundAxe", true)
				.addEffect("HasAxe", true));

		actions.add(new ActionBase("ChopLog", 4)
				.addRequirement("HasAxe", true)
				.addEffect("CanMove", false, 0.1F)
				.addEffect("MakeFirewood", true));

		actions.add(new ActionBase("HealSelf", 3)
				.addEffect("CanMove", true));

		actions.add(new ActionBase("GetFire", 1)
				.addRequirement("FoundFire", true)
				.addEffect("HasFire", true));

		actions.add(new ActionBase("FindFire", 2)
				.addRequirement("KnowLocation", true)
				.addRequirement("CanMove", true)
				.addEffect("FoundFire", true));

		actions.add(new ActionBase("MakeTorch", 2)
				.addRequirement("MakeFirewood", true)
				.addRequirement("HasFire", true)
				.addEffect("MakeTorch", true));

		//Generate biases
		biases = new ArrayList<>();

		biases.add(new BiasBase()
				.addBias("CanMove", false, 2));
	}

	public boolean hasMetGoals(World world)
	{
		for(Goal goal : this.goals)
		{
			if (!goal.isSatisfied(world)) return false;
		}

		return true;
	}

	public void step(World world, Random rand)
	{
		//if want to plan...
		Iterable<Action> actions = this.think(world);
		if (actions == null)
		{
			System.out.println("Do nothing.");
			world.add("ExitGame", true);
		}
		else
		{
			//Then do!
			for (Action action : actions)
			{
				System.out.print("Try " + action + "...");
				if (action.hasRequiredState(this, world))
				{
					System.out.println("Done!");
					action.apply(world, rand);
				}
				else
				{
					System.out.println("Failed!");
				}
			}
		}

		if (this.hasMetGoals(world))
		{
			world.add("ExitGame", true);
		}
	}

	protected Iterable<Action> think(Environment env)
	{
		GoalState goals = new GoalState(this.goals);
		return this.thinker.plan(env, goals);
	}

	public Iterable<Bias> getPossibleBiases(Environment env)
	{
		return this.biases;
	}

	public Iterable<Action> getPossibleActions(Environment env)
	{
		return this.actions;
	}
}
