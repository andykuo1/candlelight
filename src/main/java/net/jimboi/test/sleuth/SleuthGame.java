package net.jimboi.test.sleuth;

import net.jimboi.test.sleuth.cluedo.GameOfMotive;
import net.jimboi.test.sleuth.cluedo.GameOfOpportunity;
import net.jimboi.test.sleuth.cluedo.Room;
import net.jimboi.test.sleuth.cluedo.Venue;
import net.jimboi.test.sleuth.cluedo.VenueFactory;
import net.jimboi.test.sleuth.data.BodyPart;

import org.zilar.console.Console;
import org.zilar.console.ConsoleStyle;

import java.util.Random;

/**
 * Created by Andy on 9/22/17.
 */
public class SleuthGame
{
	private static SleuthGame INSTANCE;
	private static Console CONSOLE;

	public static void main(String[] args)
	{
		System.out.println("Opening...");
		CONSOLE = new Console(480, 640);
		CONSOLE.setWindowCloseHandler(console -> {
			System.out.println("Closing...");
			CONSOLE.destroy();
			System.exit(0);
			return false;
		});
		System.out.println("Running!");
		INSTANCE = new SleuthGame();
		INSTANCE.run();
	}

	private Random rand = new Random();
	private long seed = this.rand.nextLong();

	public SleuthGame()
	{
		CONSOLE.addCommand("set_seed", strings -> this.seed = Long.parseLong(strings[0]));
	}

	public void run()
	{
		ConsoleStyle.title(CONSOLE, "Welcome to Sleuth!");
		ConsoleStyle.message(CONSOLE, "Commands:");

		CONSOLE.print(" ");
		CONSOLE.beginAttributes().setActionLink(() -> {
			this.rand.setSeed(this.seed);
			generateCrimeCase(this.rand);
			this.seed = this.rand.nextLong();
		}).printEnd("new_case");
		CONSOLE.println(" - Generates a new case.");

		CONSOLE.print(" ");
		CONSOLE.beginAttributes().setActionLink(() -> {
			this.rand.setSeed(this.seed);
			generateCrimeScene(this.rand, new Case(this.rand));
			this.seed = this.rand.nextLong();
		}).printEnd("new_scene");
		CONSOLE.println(" - Generates a new crime scene.");

		CONSOLE.print(" ");
		CONSOLE.beginAttributes().setActionLink(() -> {
			this.rand.setSeed(this.seed);
			generateCluedo(this.rand);
			this.seed = this.rand.nextLong();
		}).printEnd("new_cluedo");
		CONSOLE.println(" - Generates a new cluedo.");

		CONSOLE.print(" ");
		CONSOLE.beginAttributes().setActionLink(() -> {
			this.rand.setSeed(this.seed);
			generateVenue(this.rand);
			this.seed = this.rand.nextLong();
		}).printEnd("new_venue");
		CONSOLE.println(" - Generates a new venue.");

		ConsoleStyle.newline(CONSOLE);

		ConsoleStyle.message(CONSOLE, " > Find Clues");
		ConsoleStyle.message(CONSOLE, "    - Investigate your surroundings");
		ConsoleStyle.message(CONSOLE, "    - Interrogate the suspects");
		ConsoleStyle.message(CONSOLE, " > Expose Lies");
		ConsoleStyle.message(CONSOLE, "    - Show contradictions from ANY to TRUTH");
		ConsoleStyle.message(CONSOLE, " > Establish Connections");
		ConsoleStyle.message(CONSOLE, "    - Mind Palace it up.");
		ConsoleStyle.message(CONSOLE, "    - Draw conclusions from Investigation");
		ConsoleStyle.message(CONSOLE, "    - Make Evidence");
		ConsoleStyle.message(CONSOLE, " > Follow Leads");
		ConsoleStyle.message(CONSOLE, "    - Prove Evidence");
		ConsoleStyle.message(CONSOLE, " > Make Deductions");
		ConsoleStyle.message(CONSOLE, " > Confrontation");

		ConsoleStyle.newline(CONSOLE);
	}

	private static void generateCluedo(Random rand)
	{
		ConsoleStyle.divider(CONSOLE, "=-");
		ConsoleStyle.newline(CONSOLE);

		ConsoleStyle.button(CONSOLE, "Game of Opportunity", () -> {
			GameOfOpportunity game = new GameOfOpportunity();
			game.start(CONSOLE, rand);
		});
		ConsoleStyle.button(CONSOLE, "Game of Motive", () -> {
			GameOfMotive game = new GameOfMotive();
			game.start(CONSOLE, rand);
		});

		ConsoleStyle.newline(CONSOLE);
	}

	private static void generateVenue(Random rand)
	{
		ConsoleStyle.divider(CONSOLE, "=-");
		int people = 1 + rand.nextInt(6);
		ConsoleStyle.message(CONSOLE, "People: " + people);
		Venue venue = VenueFactory.create(rand, people);
		for(Room room : venue.rooms)
		{
			ConsoleStyle.message(CONSOLE, " - " + room.toString());
		}
		ConsoleStyle.newline(CONSOLE);
	}

	private static void generateCrimeCase(Random rand)
	{
		Case crimeCase = new Case(rand);
		crimeCase.print(CONSOLE);
		crimeCase.printMurderer(CONSOLE);
		crimeCase.printVictim(CONSOLE);
		CONSOLE.println();
	}

	private static void generateCrimeScene(Random rand, Case crimeCase)
	{
		crimeCase.print(CONSOLE);

		ConsoleStyle.title(CONSOLE, "Scene #" + rand.nextInt(100));

		//Generate Crime Scene
		//Weapon
		//Method of Death

		//Pre Crime is generated.
		//The Murderer is now able to deal the killing blow to victim.

		ConsoleStyle.message(CONSOLE, "PRE-CRIME");
		ConsoleStyle.message(CONSOLE, " - The murderer runs into the crime scene.");

		ConsoleStyle.message(CONSOLE, "COMMIT-CRIME");
		//COMMIT MURDER
		int proficiency = 10; //skills, attributes, strength, intelligence
		int accuracy = 10; //nervousness, stress
		int painLevel = crimeCase.motive.getPainLevel() * 4; //motives and traits, may need some multiplier
		Weapon weapon = crimeCase.mean;

		boolean strikeagain = false;
		do
		{
			//STRIKE WITH WEAPON
			ConsoleStyle.message(CONSOLE, " - The murderer used " + crimeCase.mean + " to kill " + crimeCase.victim.name + ".");
			BodyPart part = BodyPart.values()[rand.nextInt(BodyPart.values().length)];
			ConsoleStyle.message(CONSOLE, " - The murderer stabbed the " + part + " of victim.");
			//weapon.use(crimeCase.murderer, crimeCase.victim, environment);
			//UPDATE ENVIRONMENT
			ConsoleStyle.message(CONSOLE, " - The area is sprayed with blood.");
			// - Update all sounds, people in the area, lights, smells, etc.
			// - Increase stress as well, if not natural killer
			//EVALUATE STATE
			// - let the user evaluate his situation
			// - Calculate the stress and add it.
			ConsoleStyle.message(CONSOLE, " - The murderer is under further stress.");
			int newStress = 0;
			//if nervous, or too stressed, then strikeagain = false;
			if (!testStressResolve(crimeCase.murderer, newStress))
			{
				break;
			}

			ConsoleStyle.message(CONSOLE, " - The murderer checks out the victim's health.");
			//otherwise, be logical and complete the mission
			strikeagain = !isVictimDead(crimeCase.victim); // Should be based on perception and skills
			ConsoleStyle.message(CONSOLE, " - The murderer thinks the victim is " + (strikeagain ? "not " : "") + "dead.");
			if (painLevel > 0)
			{
				ConsoleStyle.message(CONSOLE,  " - either way, the murderer wants to keep stabbing.");
				//They want to inflict pain, so just straight up keep merc'n 'em.
				strikeagain = rand.nextInt(painLevel) != 0;
			}
		}
		while(strikeagain);

		//Now if shock, the person needs to rest.
		ConsoleStyle.message(CONSOLE, " - the murderer is in shock.");
		//Otherwise, Post Crime is generated.

		ConsoleStyle.message(CONSOLE, "POST-CRIME");
		ConsoleStyle.message(CONSOLE, " - The murderer flees the crime scene.");

		ConsoleStyle.newline(CONSOLE);
	}

	private static boolean testStressResolve(Actor actor, int newStress)
	{
		return true;
	}

	private static boolean isVictimDead(Actor victim)
	{
		return true;
	}

	private static void generateActionMurder(Random rand, Weapon weapon, Actor murderer, Actor victim)
	{

	}
}
