package net.jimboi.test.sleuth;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleUtil;
import net.jimboi.test.sleuth.data.MaritalStatus;
import net.jimboi.test.sleuth.data.RelationshipStatus;
import net.jimboi.test.sleuth.data.Season;
import net.jimboi.test.sleuth.data.WeekDay;
import net.jimboi.test.sleuth.util.WeightedSet;

import java.util.Random;

/**
 * Created by Andy on 9/23/17.
 */
public class Case
{
	public int caseNumber;
	public Actor murderer;
	public Actor victim;

	public Motive motive;
	public Weapon mean;

	public String place;
	public String time;
	public WeekDay day;
	public Season season;

	public RelationshipStatus relationship;
	public int preparation;

	public Case(Random rand)
	{
		this.caseNumber = rand.nextInt(Integer.MAX_VALUE);

		int motiveIndex = rand.nextInt(Motive.values().length);
		motive = Motive.values()[motiveIndex];
		mean = MEANS.getRandomWeightedValue(rand);

		place = PLACEOFOCCURANCE.getRandomWeightedValue(rand);
		if (place.contains("Home")) place += " - " + PLACEOFHOME.getRandomWeightedValue(rand);
		time = TIMEOFDAY.getRandomWeightedValue(rand);
		day = DAYOFWEEK.getRandomWeightedValue(rand);
		season = SEASON.getRandomWeightedValue(rand);

		relationship = RELATIONSHIPS.getRandomWeightedValue(rand);
		preparation = rand.nextInt(5) + 1;

		murderer = ActorFactory.create(rand, true);
		if (relationship == RelationshipStatus.SPOUSE) murderer.maritalStatus = MaritalStatus.MARRIED;

		victim = ActorFactory.create(rand, false);
		if (relationship == RelationshipStatus.SPOUSE) victim.maritalStatus = MaritalStatus.MARRIED;
	}

	public void print(Console console)
	{
		ConsoleUtil.title(console, "Case #" + this.caseNumber);
		ConsoleUtil.message(console, "Motive: " + this.motive);
		ConsoleUtil.message(console, " - " + motive.getDefinition());
		ConsoleUtil.message(console, "Mean: " + mean);
		ConsoleUtil.message(console, "Opportunity: " + "???");
		ConsoleUtil.message(console, " - Place: " + place);
		ConsoleUtil.message(console, " - Time: " + season + " " + day + " " + time);

		ConsoleUtil.newline(console);
		ConsoleUtil.divider(console, "- ");
		ConsoleUtil.newline(console);

		ConsoleUtil.message(console, "Preparation Level: " + preparation + " / 5");
		ConsoleUtil.message(console, "Relationship With Victim: " + relationship);
		ConsoleUtil.newline(console);

		//What to do before the crime?
		//Planning -> Preparation -> Action

		//What to do after the crime?
		//Stage -> Hide -> Flee

		//How to generate Alibi?

		/*
		ConsoleHelper.divider(console, true);
		generateCleanUpCrime(rand);

		ConsoleHelper.divider(console, true);
		ConsoleHelper.message(console, "What to do after the crime?");
		ConsoleHelper.message(console, " - Stage the Crime");
		ConsoleHelper.message(console, " - Hide the Crime");
		ConsoleHelper.message(console, " - Flee the Crime");
		ConsoleHelper.message(console, "How to stage the crime?");
		ConsoleHelper.message(console, " - Stage the body");
		ConsoleHelper.message(console, " - Stage the murder weapon");
		ConsoleHelper.message(console, " - Stage the crime scene");
		ConsoleHelper.message(console, " - Put fake evidence that points to someone else");
		ConsoleHelper.message(console, " - Create an alibi for self");
		ConsoleHelper.message(console, "How to hide the crime?");
		ConsoleHelper.message(console, " - Dispose the body");
		ConsoleHelper.message(console, " - Dispose the murder weapon");
		ConsoleHelper.message(console, " - Clean the crime scene");
		ConsoleHelper.message(console, " - Create an alibi for self");
		ConsoleHelper.message(console, "How to flee the crime?");
		ConsoleHelper.message(console, " - Run.");
		ConsoleHelper.newline(console);
		*/
	}

	public void printMurderer(Console console)
	{
		ConsoleUtil.message(console, "=------------=");
		ConsoleUtil.message(console, "=- Murderer -=");
		ConsoleUtil.message(console, "=------------=");
		ConsoleUtil.message(console, murderer.toString());
	}

	public void printVictim(Console console)
	{
		ConsoleUtil.message(console, "=------------=");
		ConsoleUtil.message(console, "=-  Victim  -=");
		ConsoleUtil.message(console, "=------------=");
		ConsoleUtil.message(console, victim.toString());
	}

	public static final WeightedSet<Weapon> MEANS = new WeightedSet<>();
	static
	{
		MEANS.add(Weapon.FIREARM, 4501);
		MEANS.add(Weapon.KNIFE, 1836);
		MEANS.add(Weapon.BLUNT, 623);
		MEANS.add(Weapon.UNARMED, 817);
		MEANS.add(Weapon.POISON, 7 + 52);//Narcotics
		MEANS.add(Weapon.EXPLOSIVE, 2);
		MEANS.add(Weapon.BURNING, 98);
		MEANS.add(Weapon.DROWNING, 8);
		MEANS.add(Weapon.ROPE, 122);
		MEANS.add(Weapon.PILLOW, 84);
	}


	public static final WeightedSet<String> PLACEOFHOME = new WeightedSet<>();
	static
	{
		PLACEOFHOME.add("Bathroom", 8);
		PLACEOFHOME.add("Bedroom", 8);
		PLACEOFHOME.add("Master Bedroom", 8);
		PLACEOFHOME.add("Closet", 8);
		PLACEOFHOME.add("Dining Room", 8);
		PLACEOFHOME.add("Kitchen", 8);
		PLACEOFHOME.add("Living Room", 8);
		PLACEOFHOME.add("Front Yard", 4);
		PLACEOFHOME.add("Backyard", 8);
		PLACEOFHOME.add("Garage", 8);
		PLACEOFHOME.add("Attic", 1);
		PLACEOFHOME.add("Basement", 1);
		PLACEOFHOME.add("Den", 1);
		PLACEOFHOME.add("Hallway", 1);
		PLACEOFHOME.add("Laundry", 1);
		PLACEOFHOME.add("Office", 1);
		PLACEOFHOME.add("Pantry", 1);
		PLACEOFHOME.add("Patio", 1);
		PLACEOFHOME.add("Study Room", 1);
		PLACEOFHOME.add("Cellar", 1);
		PLACEOFHOME.add("Sun Room", 1);
		PLACEOFHOME.add("Entertainment Room", 1);
		PLACEOFHOME.add("Workshop", 1);
	}

	public static final WeightedSet<String> PLACEOFOCCURANCE = new WeightedSet<>();
	static
	{
		PLACEOFOCCURANCE.add("Street", 1097);
		PLACEOFOCCURANCE.add("Victim's Home", 548);
		PLACEOFOCCURANCE.add("Hotel / Motel", 22);
		PLACEOFOCCURANCE.add("Other's Home", 167);
		PLACEOFOCCURANCE.add("Liquor Store", 5);
		PLACEOFOCCURANCE.add("Bar", 41);
		PLACEOFOCCURANCE.add("Commercial Building", 61);
		PLACEOFOCCURANCE.add("Parking Lot", 104);
		PLACEOFOCCURANCE.add("Vehicle", 296);
		PLACEOFOCCURANCE.add("Park", 92);
		PLACEOFOCCURANCE.add("School", 3);
		PLACEOFOCCURANCE.add("Other", 28);
	}

	public static final WeightedSet<WeekDay> DAYOFWEEK = new WeightedSet<>();
	static
	{
		DAYOFWEEK.add(WeekDay.MONDAY, 43);
		DAYOFWEEK.add(WeekDay.TUESDAY, 43);
		DAYOFWEEK.add(WeekDay.WEDNESDAY, 44);
		DAYOFWEEK.add(WeekDay.THURSDAY, 42);
		DAYOFWEEK.add(WeekDay.FRIDAY, 49);
		DAYOFWEEK.add(WeekDay.SATURDAY, 57);
		DAYOFWEEK.add(WeekDay.SUNDAY, 55);

	}

	public static final WeightedSet<String> TIMEOFDAY = new WeightedSet<>();
	static
	{
		TIMEOFDAY.add("6 AM", 12);
		TIMEOFDAY.add("8 AM", 23);
		TIMEOFDAY.add("10 AM", 27);
		TIMEOFDAY.add("12 PM", 33);
		TIMEOFDAY.add("2 PM", 33);
		TIMEOFDAY.add("4 PM", 48);
		TIMEOFDAY.add("6 PM", 55);
		TIMEOFDAY.add("8 PM", 65);
		TIMEOFDAY.add("10 PM", 72);
		TIMEOFDAY.add("12 AM", 64);
		TIMEOFDAY.add("2 AM", 52);
		TIMEOFDAY.add("4 AM", 21);
		TIMEOFDAY.add("6 AM", 14);
	}

	public static final WeightedSet<Season> SEASON = new WeightedSet<>();
	static
	{
		SEASON.add(Season.WINTER, 296);
		SEASON.add(Season.SPRING, 288);
		SEASON.add(Season.SUMMER, 315);
		SEASON.add(Season.AUTUMN, 300);
	}

	public static final WeightedSet<RelationshipStatus> RELATIONSHIPS = new WeightedSet<>();
	static
	{
		RELATIONSHIPS.add(RelationshipStatus.SPOUSE, 713);
		RELATIONSHIPS.add(RelationshipStatus.PARENT, 242);
		RELATIONSHIPS.add(RelationshipStatus.CHILD, 453);
		RELATIONSHIPS.add(RelationshipStatus.SIBLING, 107);
		RELATIONSHIPS.add(RelationshipStatus.RELATIVE, 287);
		RELATIONSHIPS.add(RelationshipStatus.ACQUAINTANCE, 2723);
		RELATIONSHIPS.add(RelationshipStatus.FRIEND, 393);
		RELATIONSHIPS.add(RelationshipStatus.COUPLE, 623);
		RELATIONSHIPS.add(RelationshipStatus.NEIGHBOR, 92);
		RELATIONSHIPS.add(RelationshipStatus.EMPLOYEE, 6);
		RELATIONSHIPS.add(RelationshipStatus.EMPLOYER, 13);
		RELATIONSHIPS.add(RelationshipStatus.STRANGER, 1615);
	}

}
