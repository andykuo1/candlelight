package net.jimboi.test.gumshoe.test;

import org.bstone.console.Console;
import org.bstone.console.program.ConsoleProgram;
import org.bstone.console.style.ConsoleStyle;

/**
 * Created by Andy on 12/3/17.
 */
public class Test implements ConsoleProgram
{
	@Override
	public void main(Console con)
	{
		ConsoleStyle.title(con, "The Adventures of Detective Almond");
		con.println("You arrive at the crime scene.");
		con.println("You see the front door.");
		ConsoleStyle.button(con, "Knock on the Door", () -> {
			con.clear();
			con.println("You knock on the door.");
		});
	}

	private static Console CONSOLE;
	public static void main(String[] args)
	{
		CONSOLE = new Console("Test Console");
		ConsoleStyle.setDarkMode(CONSOLE);
		CONSOLE.writer().setTypeDelay(0.5);
		CONSOLE.start(new Test());
		System.exit(0);
	}
}
