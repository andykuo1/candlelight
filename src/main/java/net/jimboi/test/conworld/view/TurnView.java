package net.jimboi.test.conworld.view;

import net.jimboi.test.conworld.acor.Actor;
import net.jimboi.test.conworld.action.Action;
import net.jimboi.test.conworld.world.World;

import org.bstone.console.Console;
import org.bstone.console.ConsoleUtil;

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

		ConsoleUtil.title(console, "Turn: " + this.actor.getName());

		this.actor.getActions(this.actions);
		for(Action action : this.actions)
		{
			ConsoleUtil.button(console, action.getName(), () -> {
				if (this.actor.canUseAction(action))
				{
					action.execute(this.world, this.actor);
				}
				else
				{
					ConsoleUtil.message(console, "Not enough action points!");
				}
			});
		}
		this.actions.clear();

		ConsoleUtil.button(console,  "End Turn", this.world::nextTurn);

		ConsoleUtil.title(console, "Log");
	}

	@Override
	protected void terminate(Console console)
	{
		this.actor.onTurnStop(this.world);
	}
}
