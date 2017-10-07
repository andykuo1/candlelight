package net.jimboi.test.sleuth.comm.wood;

import net.jimboi.test.sleuth.comm.World;
import net.jimboi.test.sleuth.comm.goap.Action;
import net.jimboi.test.sleuth.comm.goap.ActionBase;
import net.jimboi.test.sleuth.comm.goap.Attribute;
import net.jimboi.test.sleuth.comm.goap.Environment;
import net.jimboi.test.sleuth.comm.goap.GOAPPlanner;
import net.jimboi.test.sleuth.comm.goap.GoalSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 9/27/17.
 */
public class AgentWood extends Agent
{
	private final GOAPPlanner planner;
	public Environment goals;
	public final List<Action> actions = new ArrayList<>();

	static Attribute canMove = Attribute.create("CanMove");
	static Attribute knowLocation = Attribute.create("KnowLocation");
	static Attribute foundAxe = Attribute.create("FoundAxe");
	static Attribute hasAxe = Attribute.create("HasAxe");
	static Attribute foundFire = Attribute.create("FoundFire");
	static Attribute makeFirewood = Attribute.create("MakeFirewood");
	static Attribute hasFire = Attribute.create("HasFire");
	static Attribute makeTorch = Attribute.create("MakeTorch");
	static Attribute[] torchLights = Attribute.create("TorchLight", "Dim", "Lit", "Bright", "Blinding");
	static Attribute exitGame = Attribute.create("ExitGame");

	public AgentWood()
	{
		this.planner = new GOAPPlanner(e -> this.actions);

		//Generate goals
		goals = new GoalSnapshot()
				.set(makeFirewood, true)
				.set(canMove, true)
				.set(makeTorch, true)
				.set(torchLights[2], true);
		System.out.println(goals);

		//Generate actions
		actions.add(new ActionBase("CollectBranches", 10)
				.addRequirement(canMove, true)
				.addEffect(canMove, false, 0.4F)
				.addEffect(makeFirewood));

		actions.add(new ActionBase("FindAxe", 2)
				.addRequirement(canMove, true)
				.addRequirement(knowLocation, true)
				.addEffect(foundAxe));

		actions.add(new ActionBase("LookAround", 1)
				.addEffect(knowLocation));

		actions.add(new ActionBase("GetAxe", 1)
				.addRequirement(foundAxe, true)
				.addEffect(hasAxe));

		actions.add(new ActionBase("ChopLog", 4)
				.addRequirement(hasAxe, true)
				.addEffect(canMove, false, 0.1F)
				.addEffect(makeFirewood));

		actions.add(new ActionBase("HealSelf", 3)
				.addEffect(canMove));

		actions.add(new ActionBase("GetFire", 1)
				.addRequirement(foundFire, true)
				.addEffect(hasFire));

		actions.add(new ActionBase("FindFire", 2)
				.addRequirement(knowLocation, true)
				.addRequirement(canMove, true)
				.addEffect(foundFire)
				.addEffect(torchLights[0]));

		actions.add(new ActionBase("MakeTorch", 2)
				.addRequirement(makeFirewood, true)
				.addRequirement(hasFire, true)
				.addEffect(makeTorch)
				.addEffect(torchLights[2]));
	}

	public void step(World world, Random rand)
	{
		//if want to plan...
		Iterable<Action> actions = this.think(world);
		if (actions == null)
		{
			System.out.println("Do nothing.");
			world.addAttribute(exitGame);
		}
		else
		{
			//Then do!
			for (Action action : actions)
			{
				System.out.print("Try " + action + "...");
				if (action.isSupportedConditions(world))
				{
					System.out.println("Done!");
					action.applyEffects(world, rand);
				}
				else
				{
					System.out.println("Failed!");
				}
			}
		}

		if (this.goals.isSatisfiedActionState(world))
		{
			world.addAttribute(exitGame);
		}
	}

	protected Iterable<Action> think(Environment env)
	{
		return this.planner.plan(env, this.goals);
	}

	@Override
	public Iterable<Action> getPossibleActions(Environment env)
	{
		return this.actions;
	}
}
