package net.jimboi.test.gumshoe;

import net.jimboi.test.sleuth.data.Gender;
import net.jimboi.test.sleuth.data.RelationshipStatus;
import net.jimboi.test.sleuth.data.Season;

import org.zilar.console.Console;
import org.zilar.console.ConsoleStyle;
import org.zilar.console.SimpleConsole;
import org.zilar.console.board.ConsoleState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by Andy on 12/2/17.
 */
public class GumShoe implements ConsoleState
{
	private static String[] NAMES_MALE;
	private static String[] NAMES_FEMALE;
	private static String[] PERSONALITY;
	private static String[] SECRETS_MINOR;
	private static String[] SECRETS_MAJOR;
	private static String[] GOALS;

	@Override
	public void render(Console con)
	{
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Introduction
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		con.println("Generating Murder Mystery...");

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 1
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		final Season season = choose(Season.values());
		con.println("Time: " + season + " 1950");
		con.println("Setting: Unknown");

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 2
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		final int CULPRITS = 1;
		final int VICTIMS = 1;
		final int SUSPECTS_MIN = 1;
		final int SUSPECTS_MAX = 3;

		//Generate suspect list
		final int numOfSuspects = choose(SUSPECTS_MIN, SUSPECTS_MAX) + CULPRITS + VICTIMS;
		List<Body> suspects = new ArrayList<>();
		for(int i = 0; i < numOfSuspects; ++i)
		{
			Body suspect = new Body("Character");
			Gender gender = choose(Gender.values());
			suspect.set("name", gender == Gender.MALE ? choose(NAMES_MALE) : choose(NAMES_FEMALE));
			suspect.set("gender", gender);
			suspects.add(suspect);
		}

		//Create relationship web
		for(Body suspect : suspects)
		{
			Map<Body, RelationshipStatus> relations =
					suspect.getOrCompute("relationship", (id) -> new HashMap<>());

			//Generate for each other suspect
			for(Body other : suspects)
			{
				if (other == suspect) continue;

				Map<Body, RelationshipStatus> otherRelations =
						other.getOrCompute("relationship", (id) -> new HashMap<>());

				//If there is not a relationship yet...
				if (!relations.containsKey(other))
				{
					//Make one
					RelationshipStatus status = choose(RelationshipStatus.values());
					relations.put(other, status);
					otherRelations.put(suspect, RelationshipStatus.getOtherStatus(status));
				}
			}
		}

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 3
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		//Generate Personalities
		for(Body suspect : suspects)
		{
			Set<String> personalities = new HashSet<>();
			for(int i = 0; i < 4; ++i)
			{
				personalities.add(choose(PERSONALITY));
			}
			suspect.set("personality", personalities);
		}

		//Generate Goals
		for(Body suspect : suspects)
		{
			Set<String> goals = new HashSet<>();
			for(int i = 0; i < 3; ++i)
			{
				goals.add(choose(GOALS));
			}
			suspect.set("goals", goals);
		}

		//Generate Secrets
		for(Body suspect : suspects)
		{
			//Big Secrets
			Set<String> secrets = new HashSet<>();
			for(int i = 0; i < 2; ++i)
			{
				secrets.add(choose(SECRETS_MAJOR));
			}
			suspect.set("secretMajor", secrets);

			//Small Secrets
			secrets = new HashSet<>();
			for(int i = 0; i < 1; ++i)
			{
				secrets.add(choose(SECRETS_MINOR));
			}
			suspect.set("secretMinor", secrets);
		}

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 4
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		//Generate Hooks
		for(Body suspect : suspects)
		{
			for(int i = 0; i < 6; ++i)
			{

			}
		}

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Conclusion
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		//Print everything...
		con.println();
		ConsoleStyle.title(con, "Suspects");

		for(Body suspect : suspects)
		{
			Body.println(con, suspect);
			con.println();
		}
	}

	private static int choose(int min, int max)
	{
		return min + RAND.nextInt(max - min);
	}

	private static <T> T choose(T[] array)
	{
		return array[RAND.nextInt(array.length)];
	}

	private static String[] loadFromFile(String filename)
	{
		System.out.print("Loading '" + filename + "'...");
		Path path = new File(DIRECTORY, filename).toPath();
		try
		{
			Stream<String> lines = Files.lines(path);
			String[] result = lines.toArray(String[]::new);
			System.out.println("found " + result.length + " entries!");
			return result;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("cannot find any entries!");
		return new String[0];
	}

	private static void writeToFile(String filename, Iterable<String> lines)
	{
		Path path = new File(DIRECTORY, filename).toPath();
		try
		{
			Files.write(path, lines);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static final File DIRECTORY = new File("src/main/java/net/jimboi/test/gumshoe/");
	private static final Random RAND = new Random();
	private static Console CONSOLE;

	public static void main(String[] args)
	{
		NAMES_MALE = loadFromFile("names_male.txt");
		NAMES_FEMALE = loadFromFile("names_female.txt");
		PERSONALITY = loadFromFile("personality.txt");
		SECRETS_MAJOR = loadFromFile("secrets_major.txt");
		SECRETS_MINOR = loadFromFile("secrets_minor.txt");
		GOALS = loadFromFile("goals.txt");

		CONSOLE = new SimpleConsole(480, 640, new GumShoe());
		Console.setDarkMode(CONSOLE);
		CONSOLE.setTypeWriterDelayMultiplier(0.5F);
		CONSOLE.run();
		System.exit(0);
	}
}
