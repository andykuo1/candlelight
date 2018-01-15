package net.jimboi.test.gumshoe.test.backtrack;

import net.jimboi.test.gumshoe.GumShoeProgram;
import net.jimboi.test.gumshoe.test.venue.Venue;
import net.jimboi.test.gumshoe.test.venue.layout.EntranceType;
import net.jimboi.test.gumshoe.test.venue.layout.Location;
import net.jimboi.test.gumshoe.test.venue.layout.VenueBuilder;
import net.jimboi.test.gumshoe.test.venue.layout.VenueLayout;
import net.jimboi.test.sleuth.data.RelationshipStatus;
import net.jimboi.test.sleuth.data.Season;
import net.jimboi.test.sleuth.data.Time;

import org.bstone.console.Console;
import org.bstone.console.style.ConsoleStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andy on 12/22/17.
 */
public class Backtrack extends GumShoeProgram
{
	public static final File ROOT = new File("src/main/java/net/jimboi/test/gumshoe/test/backtrack");
	public static String[] NAMES;
	public static String[] WEAPONS;
	public static String[] MOTIVES;
	public static String[] TRAITS;

	public Backtrack()
	{
		super("Night of Crime");
	}

	@Override
	protected void content(Console con)
	{
		Random rand = new Random();

		NAMES = loadFromFile(con, ROOT, "names.txt");
		WEAPONS = loadFromFile(con, ROOT, "weapons.txt");
		MOTIVES = loadFromFile(con, ROOT, "motives.txt");
		TRAITS = loadFromFile(con, ROOT, "traits.txt");

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Introduction
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		con.println("Generating Murder Mystery...");

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 1
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		final Season season = choose(rand, Season.values());
		con.println("Time: " + season + " 1950");
		con.println("Setting: Unknown");

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 2
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		List<Bobman> actors = createActors(con, rand, 3, 6);

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 3
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		Map<Bobman, Map<Bobman, RelationshipStatus>> relationshipWeb = generateRelationships(con, rand, actors);

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 4
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		//Traits
		con.println("Assigning Traits...");
		Map<Bobman, Set<String>> traits = new HashMap<>();
		for(Bobman actor : actors)
		{
			Set<String> set = traits.computeIfAbsent(actor, actor1 -> new HashSet<>());
			con.println("..." + actor.getName() + "...");
			for(int i = 0; i < 4; ++i)
			{
				String trait = choose(rand, TRAITS);
				set.add(trait);
				con.println("   ..." + trait + "...");
			}
		}
		con.println();

		//Secrets
		con.println("Making Secrets...");

		for(Bobman actor : actors)
		{
			con.println("..." + actor.getName() + "...");

			//Major
			for (int i = 0; i < 2; ++i)
			{
				String motive = choose(rand, MOTIVES);
				con.println("   ..." + motive + "...");
			}

			//Minor
			for (int i = 0; i < 1; ++i)
			{
				String motive = choose(rand, MOTIVES);
				con.println("   ..." + motive + "...");
			}
		}
		con.println();

		//Secret Tasks

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 5
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		//Story Hooks

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 6
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		VenueLayout venue = createVenue(con, rand);
		List<Location> locations = new ArrayList<>();
		for(Location location : venue.getLocations())
		{
			locations.add(location);
		}

		Location crimeScene = locations.get(rand.nextInt(locations.size()));
		Time crimeTime = new Time(rand.nextInt(24));

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 5
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		//Everyone is a liar
		//Motives
		//Either want him dead
		//Or might have killed him
		//Who are they and why are they here?
		//Problem to solve

		Bobman offender = actors.remove(rand.nextInt(actors.size()));
		Bobman victim = actors.remove(rand.nextInt(actors.size()));
		String weapon = choose(rand, WEAPONS);

		con.println();
		ConsoleStyle.divider(con, "-=");
		con.println();

		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		//  Step 6
		//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		con.println(offender.getName() + " murdered " + victim.getName() + " in the " + crimeScene + " at " + crimeTime + " with a " + weapon);

	}

	protected static VenueLayout createVenue(Console con, Random rand)
	{
		con.println("Building Venue...");

		VenueBuilder vb = new VenueBuilder();
		vb.getRoot().add("Foyer", EntranceType.DOOR);
		vb.get("Foyer").add("Hallway", EntranceType.HALL);
		vb.get("Foyer").add("Lounge", EntranceType.HALL);
		vb.get("Hallway").add("DiningRoom", EntranceType.HALL);
		vb.get("Hallway").add("Kitchen", EntranceType.DOOR);
		vb.get("Hallway").add("StudyRoom", EntranceType.DOOR);
		vb.get("Hallway").add("Bathroom", EntranceType.DOOR);
		vb.get("StudyRoom").add("Conservatory", EntranceType.DOOR);
		vb.get("StudyRoom").add("Library", EntranceType.DOOR);
		vb.get("Lounge").add("BilliardRoom", EntranceType.DOOR);
		vb.get("Lounge").add("DiningRoom", EntranceType.DOOR);
		vb.get("Kitchen").add("Kitchen", EntranceType.DOOR);

		VenueLayout venue = vb.bake();
		for(Location room : venue.getLocations())
		{
			con.println("..." + room + "...");
		}
		return venue;
	}

	protected static Venue decorateVenue(Console con, Random rand, VenueLayout layout)
	{
		con.println("...decorating venue...");
		return new Venue(layout);
	}

	protected static List<Bobman> createActors(Console con, Random rand, int min, int max)
	{
		con.println("Creating Actors...");

		List<Bobman> dst = new ArrayList<>();
		for(int i = rand.nextInt(max - min) + min; i >= 0; --i)
		{
			Bobman actor = new Bobman(choose(rand, NAMES));
			dst.add(actor);
			con.println("..." + actor.getName() + "...");
		}

		return dst;
	}

	protected static Map<Bobman, Map<Bobman, RelationshipStatus>> generateRelationships(Console con, Random rand, List<Bobman> actors)
	{
		con.println("Establishing relationships...");

		Map<Bobman, Map<Bobman, RelationshipStatus>> relationshipWeb = new HashMap<>();

		for(Bobman actor : actors)
		{
			Map<Bobman, RelationshipStatus> relations = relationshipWeb.computeIfAbsent(actor, actor1 -> new HashMap<>());

			con.println("..." + actor.getName() + "...");
			for(Bobman other : actors)
			{
				if (other == actor) continue;

				Map<Bobman, RelationshipStatus> otherRelations = relationshipWeb.computeIfAbsent(other, actor1 -> new HashMap<>());

				//If there is not a relationship yet...
				if (!relations.containsKey(other))
				{
					RelationshipStatus status = choose(rand, RelationshipStatus.values());
					relations.put(other, status);
					otherRelations.put(actor, RelationshipStatus.getOtherStatus(status));

					con.println("   ..." + status + " of " + other.getName() + "...");
				}
				else
				{
					RelationshipStatus status = relations.get(other);
					con.println("   ..." + status + " of " + other.getName() + "...");
				}
			}
		}

		return relationshipWeb;
	}

	public static void main(String[] args)
	{
		GumShoeProgram.start(new Backtrack());
	}
}
