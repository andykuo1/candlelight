package net.jimboi.test.sleuth.example;

import net.jimboi.test.sleuth.example.story.StoryFemo;

import org.zilar.console.Console;
import org.zilar.console.board.ConsoleBoard;

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

		ConsoleBoard board = new StoryFemo();
		board.start(CONSOLE);
		CONSOLE.addCommand("back", s -> board.back(CONSOLE));
		CONSOLE.run();
	}

	private Sleuth() {}
}
