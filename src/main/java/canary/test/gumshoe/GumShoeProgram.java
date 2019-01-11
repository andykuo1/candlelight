package canary.test.gumshoe;

import canary.bstone.console.Console;
import canary.bstone.console.program.ConsoleProgram;
import canary.bstone.console.state.ConsoleState;
import canary.bstone.console.style.ConsoleStyle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by Andy on 12/22/17.
 */
public abstract class GumShoeProgram extends ConsoleProgram
{
	public GumShoeProgram(String title)
	{
		super(title);
	}

	protected static String[] loadFromFile(Console con, File directory, String filename)
	{
		con.print("Loading '" + filename + "'...");
		Path path = new File(directory, filename).toPath();
		try
		{
			Stream<String> lines = Files.lines(path).filter(s -> !s.isEmpty());
			String[] result = lines.toArray(String[]::new);
			con.println("found " + result.length + " entries!");
			return result;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		con.println("cannot find any entries!");
		return new String[0];
	}

	protected static void writeToFile(Console con, File directory, String filename, Iterable<String> lines)
	{
		con.print("Writing '" + filename + "'...");
		Path path = new File(directory, filename).toPath();
		try
		{
			Files.write(path, lines);
			con.println("finished writing all entries!");
		}
		catch (IOException e)
		{
			con.println("failed to write all entries!");
			e.printStackTrace();
		}
	}

	protected static void next(Console con, ConsoleState prog)
	{
		con.waitForContinue();
		con.states().next(con, prog);
	}

	protected static void quit(Console con)
	{
		con.info("'quit' to continue");
		con.waitForNewResponse("quit"::equals);
		con.stop();
	}

	protected static int choose(Random rand, int min, int max)
	{
		return min + rand.nextInt(max - min);
	}

	protected static <T> T choose(Random rand, T[] array)
	{
		return array[rand.nextInt(array.length)];
	}

	protected static void start(GumShoeProgram program)
	{
		Console console = new Console("MurderMystery");
		ConsoleStyle.setDarkMode(console);
		console.writer().setTypeDelay(0.1);
		console.start(program);
		System.exit(0);
	}
}
