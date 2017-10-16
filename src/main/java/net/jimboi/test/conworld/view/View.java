package net.jimboi.test.conworld.view;

import org.bstone.console.Console;

/**
 * Created by Andy on 8/30/17.
 */
public abstract class View
{
	private final Console console;
	private boolean running;

	public View(Console console)
	{
		this.console = console;

		this.console.setWindowCloseHandler((c) -> {
			this.destroy();
			return false;
		});
	}

	protected abstract void initialize(Console console);
	protected abstract void terminate(Console console);

	public final void create()
	{
		this.initialize(this.console);
		this.running = true;
	}

	public final void destroy()
	{
		if (this.running)
		{
			this.terminate(this.console);
			this.running = false;
		}

		this.console.destroy();
	}

	public final boolean isRunning()
	{
		return this.running;
	}

	public final Console getConsole()
	{
		return this.console;
	}
}
