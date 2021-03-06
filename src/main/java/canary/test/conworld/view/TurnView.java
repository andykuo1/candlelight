package canary.test.conworld.view;

import canary.test.conworld.acor.Actor;
import canary.test.conworld.action.Action;
import canary.test.conworld.world.World;

import canary.zilar.console.Console;
import canary.zilar.console.ConsoleStyle;

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

		ConsoleStyle.title(console, "Turn: " + this.actor.getName());

		this.actor.getActions(this.actions);
		for(Action action : this.actions)
		{
			ConsoleStyle.button(console, action.getName(), () -> {
				if (this.actor.canUseAction(action))
				{
					action.execute(this.world, this.actor);
				}
				else
				{
					ConsoleStyle.message(console, "Not enough action points!");
				}
			});
		}
		this.actions.clear();

		ConsoleStyle.button(console,  "End Turn", this.world::nextTurn);

		ConsoleStyle.title(console, "Log");
	}

	@Override
	protected void terminate(Console console)
	{
		this.actor.onTurnStop(this.world);
	}
}
