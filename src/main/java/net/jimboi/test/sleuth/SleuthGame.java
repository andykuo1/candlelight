package net.jimboi.test.sleuth;

import net.jimboi.test.sleuth.cluedo.GameOfMotive;
import net.jimboi.test.sleuth.cluedo.GameOfOpportunity;
import net.jimboi.test.sleuth.cluedo.Room;
import net.jimboi.test.sleuth.cluedo.Venue;
import net.jimboi.test.sleuth.cluedo.VenueFactory;
import net.jimboi.test.sleuth.data.BodyPart;

import org.bstone.console.Console;
import org.bstone.console.ConsoleUtil;

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
		ConsoleUtil.title(CONSOLE, "Welcome to Sleuth!");
		ConsoleUtil.message(CONSOLE, "Commands:");

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

		ConsoleUtil.newline(CONSOLE);

		ConsoleUtil.message(CONSOLE, " > Find Clues");
		ConsoleUtil.message(CONSOLE, "    - Investigate your surroundings");
		ConsoleUtil.message(CONSOLE, "    - Interrogate the suspects");
		ConsoleUtil.message(CONSOLE, " > Expose Lies");
		ConsoleUtil.message(CONSOLE, "    - Show contradictions from ANY to TRUTH");
		ConsoleUtil.message(CONSOLE, " > Establish Connections");
		ConsoleUtil.message(CONSOLE, "    - Mind Palace it up.");
		ConsoleUtil.message(CONSOLE, "    - Draw conclusions from Investigation");
		ConsoleUtil.message(CONSOLE, "    - Make Evidence");
		ConsoleUtil.message(CONSOLE, " > Follow Leads");
		ConsoleUtil.message(CONSOLE, "    - Prove Evidence");
		ConsoleUtil.message(CONSOLE, " > Make Deductions");
		ConsoleUtil.message(CONSOLE, " > Confrontation");

		ConsoleUtil.newline(CONSOLE);
	}

	private static void generateCluedo(Random rand)
	{
		ConsoleUtil.divider(CONSOLE, "=-");
		ConsoleUtil.newline(CONSOLE);

		ConsoleUtil.button(CONSOLE, "Game of Opportunity", () -> {
			GameOfOpportunity game = new GameOfOpportunity();
			game.start(CONSOLE, rand);
		});
		ConsoleUtil.button(CONSOLE, "Game of Motive", () -> {
			GameOfMotive game = new GameOfMotive();
			game.start(CONSOLE, rand);
		});

		ConsoleUtil.newline(CONSOLE);
	}

	private static void generateVenue(Random rand)
	{
		ConsoleUtil.divider(CONSOLE, "=-");
		int people = 1 + rand.nextInt(6);
		ConsoleUtil.message(CONSOLE, "People: " + people);
		Venue venue = VenueFactory.create(rand, people);
		for(Room room : venue.rooms)
		{
			ConsoleUtil.message(CONSOLE, " - " + room.toString());
		}
		ConsoleUtil.newline(CONSOLE);
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

		ConsoleUtil.title(CONSOLE, "Scene #" + rand.nextInt(100));

		//Generate Crime Scene
		//Weapon
		//Method of Death

		//Pre Crime is generated.
		//The Murderer is now able to deal the killing blow to victim.

		ConsoleUtil.message(CONSOLE, "PRE-CRIME");
		ConsoleUtil.message(CONSOLE, " - The murderer runs into the crime scene.");

		ConsoleUtil.message(CONSOLE, "COMMIT-CRIME");
		//COMMIT MURDER
		int proficiency = 10; //skills, attributes, strength, intelligence
		int accuracy = 10; //nervousness, stress
		int painLevel = crimeCase.motive.getPainLevel() * 4; //motives and traits, may need some multiplier
		Weapon weapon = crimeCase.mean;

		boolean strikeagain = false;
		do
		{
			//STRIKE WITH WEAPON
			ConsoleUtil.message(CONSOLE, " - The murderer used " + crimeCase.mean + " to kill " + crimeCase.victim.name + ".");
			BodyPart part = BodyPart.values()[rand.nextInt(BodyPart.values().length)];
			ConsoleUtil.message(CONSOLE, " - The murderer stabbed the " + part + " of victim.");
			//weapon.use(crimeCase.murderer, crimeCase.victim, environment);
			//UPDATE ENVIRONMENT
			ConsoleUtil.message(CONSOLE, " - The area is sprayed with blood.");
			// - Update all sounds, people in the area, lights, smells, etc.
			// - Increase stress as well, if not natural killer
			//EVALUATE STATE
			// - let the user evaluate his situation
			// - Calculate the stress and add it.
			ConsoleUtil.message(CONSOLE, " - The murderer is under further stress.");
			int newStress = 0;
			//if nervous, or too stressed, then strikeagain = false;
			if (!testStressResolve(crimeCase.murderer, newStress))
			{
				break;
			}

			ConsoleUtil.message(CONSOLE, " - The murderer checks out the victim's health.");
			//otherwise, be logical and complete the mission
			strikeagain = !isVictimDead(crimeCase.victim); // Should be based on perception and skills
			ConsoleUtil.message(CONSOLE, " - The murderer thinks the victim is " + (strikeagain ? "not " : "") + "dead.");
			if (painLevel > 0)
			{
				ConsoleUtil.message(CONSOLE,  " - either way, the murderer wants to keep stabbing.");
				//They want to inflict pain, so just straight up keep merc'n 'em.
				strikeagain = rand.nextInt(painLevel) != 0;
			}
		}
		while(strikeagain);

		//Now if shock, the person needs to rest.
		ConsoleUtil.message(CONSOLE, " - the murderer is in shock.");
		//Otherwise, Post Crime is generated.

		ConsoleUtil.message(CONSOLE, "POST-CRIME");
		ConsoleUtil.message(CONSOLE, " - The murderer flees the crime scene.");

		ConsoleUtil.newline(CONSOLE);
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
