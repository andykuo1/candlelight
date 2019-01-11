package canary.test.conworld.view;

import canary.test.conworld.acor.Actor;
import canary.test.conworld.acor.Stats;

import canary.zilar.console.Console;
import canary.zilar.console.ConsoleStyle;

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
		ConsoleStyle.title(console, this.actor.getName());

		ConsoleStyle.button(console, "Refresh", this::refresh);

		final Stats stats = this.actor.getStats();
		for(String stat : stats)
		{
			ConsoleStyle.message(console, stat + " = " + stats.get(stat));
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
