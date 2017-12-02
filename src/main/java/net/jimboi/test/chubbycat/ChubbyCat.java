package net.jimboi.test.chubbycat;

import org.bstone.console.Console;
import org.bstone.console.SimpleConsole;

/**
 * Created by Andy on 11/29/17.
 */
public class ChubbyCat
{
	public static String HOST = "127.0.0.1";
	public static int PORT = 8080;

	public static Console CONSOLE;

	public static void main(String[] args)
	{
		CONSOLE = new SimpleConsole(420, 480, new MainState());
		CONSOLE.setWindowCloseHandler(console -> {
			CONSOLE.destroy();
			System.exit(0);
			return false;
		});
		CONSOLE.setTypeWriterMode(true);
		Console.setDarkMode(CONSOLE);

		CONSOLE.run();
	}
}
