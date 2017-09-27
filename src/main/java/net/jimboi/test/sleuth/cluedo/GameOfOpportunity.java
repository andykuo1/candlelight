package net.jimboi.test.sleuth.cluedo;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleHelper;
import net.jimboi.test.sleuth.Actor;
import net.jimboi.test.sleuth.ActorFactory;
import net.jimboi.test.sleuth.data.Time;

import org.bstone.util.Pair;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andy on 9/23/17.
 */
public class GameOfOpportunity extends Cluedo
{
	private Venue venue;
	private Set<Actor> suspects = new HashSet<>();
	private Actor murderer;
	private Actor victim;
	private Time timeOfDeath;

	@Override
	protected void create(Random rand)
	{
		//This is an example where anyone has the MEANS to kill anyone in the same room, without any evidence.
		//So the only way to identify the Murderer is by OPPORTUNITY and MOTIVE.
		//Opportunity can be solved by figuring out time of death, then the people in that room at the time.
		//That just leaves MOTIVE.

		//To have a murder you must have: MOTIVE, MEANS, OPPORTUNITY, and PROOF
		//MOTIVE - must have a reason
		//MEANS - must be accessed and used
		//OPPORTUNITY - must be known beforehand (fabricated or known)
		//PROOF - this is the aftermath and the reasons

		//It's all about the system of contradictions, setup by the murderer
		//So the murderer has to try to hide: MOTIVE, MEANS, and OPPORTUNITY
		//To hide these is to hide the related PROOF.

		//Hiding PROOF, is either:
		//      make it unreliable
		//      prevent its discovery
		//      point it to another
		//      alter it away from PROOF

		//Interrogations are the core interface:
		// - Use for the reaction and knowledge
		// - All questions that one can ask.
		//      What do you think about this... (to establish what is the relationship)
		//          - I don't know
		//          - I heard of a little
		//          - I know of
		//          - I know a little
		//          - I know
		//          - I know quite well
		//          - I know well
		//          - I know very well
		//      What do you remember about...
		//      What happened?

		//There are 4 times they can try to hide them:
		// - In Pre Crime
		//      This requires planning, intelligence
		//      Starts with the spark of MOTIVE
		//      Ends with the moment of OPPORTUNITY
		// - In Crime Action
		//      This requires initiative (speed of action), strength
		//      Ends with the Victim's Death
		// - In Post Crime
		//      This requires discretion, mentality
		//      Ends with the Reponse of Investigation or Discovery of Crime
		// - On Investigation
		//      This requires spontaneity (speed of change), charisma

		//In this example:
		//The MEANS is already discovered.
		//The MOTIVE is unknown.
		//The OPPORTUNITY is manipulated on investigation.


		//If no one saw, they will lie about seeing the person in the first place.
		//If people saw, then they won't lie, but saw the same as everyone else.


		//Generate Actors
		this.murderer = ActorFactory.create(rand, true);
		this.victim = ActorFactory.create(rand, false);
		for(int i = rand.nextInt(4) + 4; i > 0; --i)
		{
			this.suspects.add(ActorFactory.create(rand, false));
		}
		this.suspects.add(this.murderer);
		this.suspects.add(this.victim);

		//Generate Rooms
		this.venue = VenueFactory.create(rand, 1 + rand.nextInt(this.suspects.size() / 2));
		for(Actor suspect : this.suspects)
		{
			suspect.location = this.venue.rooms.get(rand.nextInt(this.venue.rooms.size()));
		}

		//Start Simulation
		boolean firstRound = true;
		boolean isVictimDead = false;
		Time time = new Time(rand.nextInt(Time.HOURS_IN_DAY));
		Time inc = new Time(1);
		while(!isVictimDead)
		{
			time.add(inc, time);

			//Update actors
			for(Actor suspect : this.suspects)
			{
				suspect.memories.memorize(time, "\nIt was around " + time.getHour() + ".");

				if (rand.nextBoolean())
				{
					suspect.location = this.venue.rooms.get(rand.nextInt(this.venue.rooms.size()));
					suspect.memories.memorize(time, "I went to the " + suspect.location + ".");
				}
				else
				{
					suspect.memories.memorize(time, "I remembered I was in the " + suspect.location);
				}
			}

			//Update actor memory
			for(Actor suspect : this.suspects)
			{
				if (suspect == this.victim && isVictimDead) continue;

				for(Actor other : this.suspects)
				{
					if (other == suspect) continue;
					if (other.location == suspect.location)
					{
						if (!firstRound && suspect == this.murderer && other == this.victim && rand.nextInt(3) == 0)
						{
							//SHANK THE GUY
							isVictimDead = true;
						}
						else
						{
							//THIS IS THE LIE! (by the murderer) through omission.
							suspect.memories.memorize(time, other.name + " was there.");
						}
					}
				}
				suspect.memories.memorize(time, "And that was about it.");
			}

			//Update victim health
			if (firstRound) firstRound = false;
		}
		this.timeOfDeath = new Time(time);
		this.suspects.remove(this.victim);
	}

	@Override
	protected void play(Console console)
	{
		this.main(console, new Random());
	}

	private void main(Console console, Random rand)
	{
		console.clear();
		ConsoleHelper.title(console, "Cluedo");
		ConsoleHelper.message(console, "Welcome Detective. What would you like to do first?");

		ConsoleHelper.newline(console);

		console.print(" > ").beginAttributes().setActionLink(con -> lookAround(con, rand)).printlnEnd("Look Around");
		console.print(" > ").beginAttributes().setActionLink(con -> quit(con, rand)).printlnEnd("Give Me the Answer");

		ConsoleHelper.newline(console);

		ConsoleHelper.message(console, this.venue.rooms.toString());

		ConsoleHelper.newline(console);

		ConsoleHelper.divider(console, true);
	}

	private void lookAround(Console console, Random rand)
	{
		ConsoleHelper.message(console, "You look around the " + this.victim.location + ".");
		ConsoleHelper.newline(console);

		console.print("You see ").beginAttributes().setActionLink(con -> this.inspectBody(console, rand)).printEnd(this.victim.name).println(" is dead.");
		for(Actor actor : this.suspects)
		{
			console.print("You see ").beginAttributes().setActionLink(con -> this.talkTo(console, rand, actor)).printEnd(actor.name).println(".");
		}

		ConsoleHelper.divider(console, true);
	}

	private void quit(Console console, Random rand)
	{
		ConsoleHelper.message(console, "You decide to get the answer.");
		ConsoleHelper.newline(console);

		ConsoleHelper.message(console,  this.murderer.name + " was the murderer!");

		ConsoleHelper.divider(console, true);
		console.print(" > ").beginAttributes().setActionLink(con -> main(con, rand)).printlnEnd("Back");
	}

	private void talkTo(Console console, Random rand, Actor actor)
	{
		ConsoleHelper.message(console, "You talk to " + actor.name + ".");
		ConsoleHelper.newline(console);

		ConsoleHelper.message(console, "Hello. What do you want?");

		console.print(" > ").beginAttributes().setActionLink(con -> this.showMemoryOf(console, rand, actor, this.victim)).printEnd("What can you tell me about the victim?").println();
		console.print(" > ").beginAttributes().setActionLink(con -> this.showMemoryAt(console, rand, actor, this.timeOfDeath)).printEnd("What happened at " + this.timeOfDeath.getHour() + "?").println();
		console.print(" > ").beginAttributes().setActionLink(con -> this.showMemory(console, rand, actor)).printEnd("What do you remember?").println();

		//What do you want me to ask?
		//(use evidence to ask.)

		//What happened at the crime?
		//What do you know(think) about this person?

		ConsoleHelper.divider(console, true);
	}

	private void inspectBody(Console console, Random rand)
	{
		ConsoleHelper.message(console, "You inspect the body of " + this.victim.name + ".");
		ConsoleHelper.newline(console);

		ConsoleHelper.message(console, "You find the victim's body in the " + this.victim.location + ".");
		ConsoleHelper.message(console, "You find the murder weapon!");
		ConsoleHelper.message(console, "Anyone could have used it...");
		ConsoleHelper.message(console, "But you now know the time of death.");
		ConsoleHelper.message(console, this.timeOfDeath.toString());

		ConsoleHelper.divider(console, true);
		console.print(" > ").beginAttributes().setActionLink(con -> main(con, rand)).printlnEnd("Back");
	}

	private void showMemory(Console console, Random rand, Actor actor)
	{
		ConsoleHelper.newline(console);

		for(Pair<Time, String> entry : actor.memories.data)
		{
			ConsoleHelper.message(console, entry.getSecond());
		}

		ConsoleHelper.divider(console, true);
		console.print(" > ").beginAttributes().setActionLink(con -> main(con, rand)).printlnEnd("Back");
	}

	private void showMemoryOf(Console console,  Random rand, Actor actor, Actor target)
	{
		ConsoleHelper.newline(console);

		Set<Time> times = new TreeSet<>();
		for(Pair<Time, String> entry : actor.memories.data)
		{
			if (entry.getSecond().contains(target.name))
			{
				times.add(entry.getFirst());
			}
		}

		if (times.isEmpty())
		{
			ConsoleHelper.message(console, "Sorry, I don't think I ever saw that person.");
		}
		else
		{
			ConsoleHelper.message(console, "Yeah, I remember.");

			for (Pair<Time, String> entry : actor.memories.data)
			{
				if (times.contains(entry.getFirst()))
				{
					ConsoleHelper.message(console, entry.getSecond());
				}
			}
		}

		ConsoleHelper.divider(console, true);
		console.print(" > ").beginAttributes().setActionLink(con -> main(con, rand)).printlnEnd("Back");
	}

	private void showMemoryAt(Console console, Random rand, Actor actor, Time time)
	{
		ConsoleHelper.newline(console);

		boolean dirty = false;

		for(Pair<Time, String> entry : actor.memories.data)
		{
			if (entry.getFirst().equals(time))
			{
				if (!dirty)
				{
					dirty = true;
					ConsoleHelper.message(console, "Yeah, I remember.");
				}

				String memory = entry.getSecond();
				ConsoleHelper.message(console, memory);
			}
		}

		if (!dirty)
		{
			ConsoleHelper.message(console, "Sorry, I don't remember anything at " + time.getHour() + ".");
		}

		ConsoleHelper.divider(console, true);
		console.print(" > ").beginAttributes().setActionLink(con -> main(con, rand)).printlnEnd("Back");
	}
}
