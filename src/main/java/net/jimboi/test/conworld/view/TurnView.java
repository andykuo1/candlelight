package net.jimboi.test.conworld.view;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleHelper;
import net.jimboi.test.conworld.acor.Actor;
import net.jimboi.test.conworld.action.Action;
import net.jimboi.test.conworld.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 8/30/17.
 */
public class TurnView extends View
{
	protected World world;
	protected Actor actor;

	private final List<Action> actions = new ArrayList<>();

	public TurnView(Console console, World world, Actor actor)
	{
		super(console);

		this.world = world;
		this.actor = actor;
	}

	@Override
	protected void initialize(Console console)
	{
		this.actor.onTurnStart(this.world);

		ConsoleHelper.title(console, "Turn: " + this.actor.getName());

		this.actor.getActions(this.actions);
		for(Action action : this.actions)
		{
			ConsoleHelper.button(console, action.getName(), () -> {
				if (this.actor.canUseAction(action))
				{
					action.execute(this.world, this.actor);
				}
				else
				{
					ConsoleHelper.message(console, "Not enough action points!");
				}
			});
		}
		this.actions.clear();

		ConsoleHelper.button(console,  "End Turn", this.world::nextTurn);

		ConsoleHelper.title(console, "Log");
	}

	@Override
	protected void terminate(Console console)
	{
		this.actor.onTurnStop(this.world);
	}
}
