package net.jimboi.test.conworld.view;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleHelper;
import net.jimboi.test.conworld.acor.Actor;
import net.jimboi.test.conworld.acor.Stats;

/**
 * Created by Andy on 8/30/17.
 */
public class StatView extends View
{
	protected final Actor actor;

	public StatView(Actor actor)
	{
		super(new Console(320, 480));

		this.actor = actor;
	}

	public void refresh()
	{
		this.getConsole().clear();
		this.initialize(this.getConsole());
	}

	@Override
	protected void initialize(Console console)
	{
		ConsoleHelper.title(console, this.actor.getName());

		ConsoleHelper.button(console, "Refresh", this::refresh);

		final Stats stats = this.actor.getStats();
		for(String stat : stats)
		{
			ConsoleHelper.message(console, stat + " = " + stats.get(stat));
		}
	}

	@Override
	protected void terminate(Console console)
	{
	}

	public final Actor getActor()
	{
		return this.actor;
	}
}
