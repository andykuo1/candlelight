package net.jimboi.test.sleuth;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleHelper;
import net.jimboi.test.sleuth.cluedo.GameOfMotive;
import net.jimboi.test.sleuth.cluedo.GameOfOpportunity;
import net.jimboi.test.sleuth.cluedo.Room;
import net.jimboi.test.sleuth.cluedo.Venue;
import net.jimboi.test.sleuth.cluedo.VenueFactory;
import net.jimboi.test.sleuth.data.BodyPart;

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
		CONSOLE.addCommandHandler("set_seed", strings -> this.seed = Long.parseLong(strings[0]));
	}

	public void run()
	{
		ConsoleHelper.title(CONSOLE, "Welcome to Sleuth!");
		ConsoleHelper.message(CONSOLE, "Commands:");

		CONSOLE.print(" ");
		CONSOLE.beginAttributes().setActionLink(con -> {
			this.rand.setSeed(this.seed);
			generateCrimeCase(this.rand);
			this.seed = this.rand.nextLong();
		}).printEnd("new_case");
		CONSOLE.println(" - Generates a new case.");

		CONSOLE.print(" ");
		CONSOLE.beginAttributes().setActionLink(con -> {
			this.rand.setSeed(this.seed);
			generateCrimeScene(this.rand, new Case(this.rand));
			this.seed = this.rand.nextLong();
		}).printEnd("new_scene");
		CONSOLE.println(" - Generates a new crime scene.");

		CONSOLE.print(" ");
		CONSOLE.beginAttributes().setActionLink(con -> {
			this.rand.setSeed(this.seed);
			generateCluedo(this.rand);
			this.seed = this.rand.nextLong();
		}).printEnd("new_cluedo");
		CONSOLE.println(" - Generates a new cluedo.");

		CONSOLE.print(" ");
		CONSOLE.beginAttributes().setActionLink(con -> {
			this.rand.setSeed(this.seed);
			generateVenue(this.rand);
			this.seed = this.rand.nextLong();
		}).printEnd("new_venue");
		CONSOLE.println(" - Generates a new venue.");

		ConsoleHelper.newline(CONSOLE);

		ConsoleHelper.message(CONSOLE, " > Find Clues");
		ConsoleHelper.message(CONSOLE, "    - Investigate your surroundings");
		ConsoleHelper.message(CONSOLE, "    - Interrogate the suspects");
		ConsoleHelper.message(CONSOLE, " > Expose Lies");
		ConsoleHelper.message(CONSOLE, "    - Show contradictions from ANY to TRUTH");
		ConsoleHelper.message(CONSOLE, " > Establish Connections");
		ConsoleHelper.message(CONSOLE, "    - Mind Palace it up.");
		ConsoleHelper.message(CONSOLE, "    - Draw conclusions from Investigation");
		ConsoleHelper.message(CONSOLE, "    - Make Evidence");
		ConsoleHelper.message(CONSOLE, " > Follow Leads");
		ConsoleHelper.message(CONSOLE, "    - Prove Evidence");
		ConsoleHelper.message(CONSOLE, " > Make Deductions");
		ConsoleHelper.message(CONSOLE, " > Confrontation");

		ConsoleHelper.newline(CONSOLE);
	}

	private static void generateCluedo(Random rand)
	{
		ConsoleHelper.divider(CONSOLE, false);
		ConsoleHelper.newline(CONSOLE);

		ConsoleHelper.button(CONSOLE, "Game of Opportunity", () -> {
			GameOfOpportunity game = new GameOfOpportunity();
			game.start(CONSOLE, rand);
		});
		ConsoleHelper.button(CONSOLE, "Game of Motive", () -> {
			GameOfMotive game = new GameOfMotive();
			game.start(CONSOLE, rand);
		});

		ConsoleHelper.newline(CONSOLE);
	}

	private static void generateVenue(Random rand)
	{
		ConsoleHelper.divider(CONSOLE, false);
		int people = 1 + rand.nextInt(6);
		ConsoleHelper.message(CONSOLE, "People: " + people);
		Venue venue = VenueFactory.create(rand, people);
		for(Room room : venue.rooms)
		{
			ConsoleHelper.message(CONSOLE, " - " + room.toString());
		}
		ConsoleHelper.newline(CONSOLE);
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

		ConsoleHelper.title(CONSOLE, "Scene #" + rand.nextInt(100));

		//Generate Crime Scene
		//Weapon
		//Method of Death

		//Pre Crime is generated.
		//The Murderer is now able to deal the killing blow to victim.

		ConsoleHelper.message(CONSOLE, "PRE-CRIME");
		ConsoleHelper.message(CONSOLE, " - The murderer runs into the crime scene.");

		ConsoleHelper.message(CONSOLE, "COMMIT-CRIME");
		//COMMIT MURDER
		int proficiency = 10; //skills, attributes, strength, intelligence
		int accuracy = 10; //nervousness, stress
		int painLevel = crimeCase.motive.getPainLevel() * 4; //motives and traits, may need some multiplier
		Weapon weapon = crimeCase.mean;

		boolean strikeagain = false;
		do
		{
			//STRIKE WITH WEAPON
			ConsoleHelper.message(CONSOLE, " - The murderer used " + crimeCase.mean + " to kill " + crimeCase.victim.name + ".");
			BodyPart part = BodyPart.values()[rand.nextInt(BodyPart.values().length)];
			ConsoleHelper.message(CONSOLE, " - The murderer stabbed the " + part + " of victim.");
			//weapon.use(crimeCase.murderer, crimeCase.victim, environment);
			//UPDATE ENVIRONMENT
			ConsoleHelper.message(CONSOLE, " - The area is sprayed with blood.");
			// - Update all sounds, people in the area, lights, smells, etc.
			// - Increase stress as well, if not natural killer
			//EVALUATE STATE
			// - let the user evaluate his situation
			// - Calculate the stress and add it.
			ConsoleHelper.message(CONSOLE, " - The murderer is under further stress.");
			int newStress = 0;
			//if nervous, or too stressed, then strikeagain = false;
			if (!testStressResolve(crimeCase.murderer, newStress))
			{
				break;
			}

			ConsoleHelper.message(CONSOLE, " - The murderer checks out the victim's health.");
			//otherwise, be logical and complete the mission
			strikeagain = !isVictimDead(crimeCase.victim); // Should be based on perception and skills
			ConsoleHelper.message(CONSOLE, " - The murderer thinks the victim is " + (strikeagain ? "not " : "") + "dead.");
			if (painLevel > 0)
			{
				ConsoleHelper.message(CONSOLE,  " - either way, the murderer wants to keep stabbing.");
				//They want to inflict pain, so just straight up keep merc'n 'em.
				strikeagain = rand.nextInt(painLevel) != 0;
			}
		}
		while(strikeagain);

		//Now if shock, the person needs to rest.
		ConsoleHelper.message(CONSOLE, " - the murderer is in shock.");
		//Otherwise, Post Crime is generated.

		ConsoleHelper.message(CONSOLE, "POST-CRIME");
		ConsoleHelper.message(CONSOLE, " - The murderer flees the crime scene.");

		ConsoleHelper.newline(CONSOLE);
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
