package net.jimboi.test.sleuth.base;

import net.jimboi.test.console.Console;

/**
 * Created by Andy on 10/6/17.
 */
public class Sleuth
{
	public static Console getConsole()
	{
		return CONSOLE;
	}

	private static Console CONSOLE;

	public static void main(String[] args)
	{
		CONSOLE = new Console(420, 480);
		CONSOLE.setWindowCloseHandler(console -> {
			CONSOLE.destroy();
			System.exit(0);
			return false;
		});
		CONSOLE.setTypeWriterMode(true);

		Console.setDarkMode(CONSOLE);

		final Game game = new Game();
		GameState state = new StateMenu(game);
		state.start(CONSOLE);
		CONSOLE.run();
	}

	private Sleuth() {}
}
