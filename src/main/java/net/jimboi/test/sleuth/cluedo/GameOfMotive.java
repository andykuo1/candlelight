package net.jimboi.test.sleuth.cluedo;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleHelper;
import net.jimboi.test.sleuth.Actor;
import net.jimboi.test.sleuth.ActorFactory;
import net.jimboi.test.sleuth.Motive;

import java.awt.Color;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andy on 9/24/17.
 */
public class GameOfMotive extends Cluedo
{
	private Set<Actor> suspects = new HashSet<>();
	private Actor murderer;
	private Actor victim;

	private Room room;

	private Motive motive;

	@Override
	public void create(Random rand)
	{
		//Create Actors
		this.murderer = ActorFactory.create(rand, true);
		this.victim = ActorFactory.create(rand, false);
		for(int i = rand.nextInt(4) + 2; i > 0; --i)
		{
			this.suspects.add(ActorFactory.create(rand, false));
		}
		this.suspects.add(this.murderer);
		this.suspects.add(this.victim);

		//Create Rooms
		this.room = RoomFactory.create(rand, RoomType.values()[rand.nextInt(RoomType.values().length)]);

		//Get Motive
		this.motive = Motive.REVENGE;
		//this.motive = Motive.values()[rand.nextInt(Motive.values().length)];

		//REVENGE
		/*

		Effects:
		 - Trusted people know about the Murderer's feelings against the Victim
		    - Very Trusted people have Knowledge of the Event
		    - People have Insight into the Murderer's relationship with the Victim
		 - People who witness the Event know the Murderer's feelings against the Victim
		    - People have Knowledge of the Event
		    - People have Insight into the Murderer's relationship with the Victim
		 - Feels pity for those similar to the Murderer
		    - Positive Relationship for similar Traits / Affiliations for the Murderer
		 - Feels betrayed for those similar to the Victim
		    - Negative Relationship for similar Traits / Affiliations for the Victim
		 - There will be a lesson taught by Death
		    - If Sociable, will Restrain and Interrogate before death
		    - May be Torture before death
		    - May be Humiliation after death
		        - Requires Public awareness

		Goals:
		 - If Sociable, will want to Restrain the Victim
		 - If Sociable, will then want to Interrogate the Victim
		 - By chance, may want to Torture the Victim before death
		    - By Mutilation
		 - Then, Kill the Victim
		 - By chance, may want to Humiliate the Victim after death
		    - By Mutilation
		    - By Body Arrangement (should be ironic)
		 - Depending on Intelligence, will want to Remove / Avoid the Effects tagged with Evidence.

		Evaluate:
		 - Interactable Objects
		 - Other Actors
		 - Environment
		 - Knowledge

		Available Actions:
		 - Look Around
			 - Interactable Objects (remember the type)
			 - Other Actors (remember the person, demeanor, and maybe appearance)
			 - Environment (remember the name)

		 - Inspect Object
		 - Move To Object (costs movement)
		    - Closely Inspect Object
		    - Open Object (if container)
		    - Grab Object
		        - Put Object in Inventory
		        - Put down Object
		    - Use Object
		    - Hit Object
		    - Hide In Object (if large container)

		 - Grab Object in Inventory (Fast, Secretly, etc.)
		    - Closely Inspect Object (will reveal)
		    - Put Object in Inventory
		    - Put down Object
		    - Use Object
		    - Destroy Object

		 - Inspect Actor
		 - Move To Actor (costs movement)
		    - Closely Inspect Actor
		    - Talk To Actor
		        - Tell Info to Actor (if bad = argument, if good = compliment, else = discussion)
		            - Can reveal other info from Actor
		        - Ask Info from Actor
		        - Persuade Knowledge for Actor
		        - (if good relationship / persuasion) Give / Remove Goal to Actor
	     - Move Into Actor (if aware, then if bad = Aggressive, if good = nothing, else = Defensive)
	        - Whisper To Actor (cannot be overhead by People)
	        - Take / Put into Actor's Inventory (slight of hand)
	        - Grapple Actor
	            - Silence Actor (if has gag in Inventory)
	            - Restrain Actor (if has rope in Inventory)
	            - Release Actor
	            - Put down Actor
	            - Talk / Whisper To Actor...
	        - Hit Actor

	     - Change Demeanor (Moody, Welcoming, etc. to grab / avoid attention) (based on social skills)

	     - Leave / Goto Room (influenced by goals or by environment)
		*/


		//For the REVENGE CASE only
		//What did the victim do to cross the murderer?

		//response to anger, injury, or humiliation
		//to transform shame into pride

		//Actions that lead to feeling humiliated, powerless, foolish, ridiculous, stupid, ashamed

		/*

		“Who, what, when, where, why, and how?”
		//Motive, Means, Opportunity, Proof, & Intent


		REVENGE
		- Form of justice

		Humiliation
		 - Intimidation,
		 - physical or mental mistreatment,
		 - trickery,
		 - embarrassment if a person is revealed to have committed a socially or legally unacceptable act -> Humiliation
		 - institutional prejudices

		 - existance of value system

		 - public failure of one's status claims vs self-esteem

		Powerless



		Foolish
		Stupid
		Ashamed


		// - Abuse / Oppression -> Powerless
		//Actions that lead to unjust loss, injury, suffering
		// - What is just action?
		*/

		/*

		They feel they have been attacked and suffered some unjust loss or injury. As a result they are feeling anger, hate, jealousy, envy, or shame.
		They are humiliated, especially if they are made to feel powerless, foolish, ridiculous, stupid, or ashamed.
		The goal of revenge is to erase shame and humiliation and restore pride.


		Goals:
			Restore dignity
			Restore honor of associated group
			Remember the damage to allies
			Teach a lesson to the aggressor
			Obtain acknowledgement from the aggressor that they were wrong and they feel remorse
			Obtain a sincere apology and know the aggressor is remorseful
			Demonstrate their power so they no longer feel powerless
			Demonstrate their viewpoint
			Obtain reparations; get paid back for their losses, and settle the score
			Make the aggressor suffer and feel their pain,
			Act as a deterrent to predatory behavior,

		*/

		//Dead Victim is no longer a suspect...
		this.suspects.remove(this.victim);
	}

	@Override
	public void play(Console out)
	{
		this.main(out, new Random());
	}

	private void main(Console out, Random rand)
	{
		out.clear();
		ConsoleHelper.title(out, this.room.toString());
		{
			out.beginAttributes().setActionLink(con -> this.evaluate(out, rand, this.victim)).setColor(Color.RED).printEnd(this.victim.name).println(" has been murdered! But why?");

			ConsoleHelper.button(out, "Who did it?", () -> this.solveMystery(out));

			ConsoleHelper.newline(out);

			ConsoleHelper.message(out, "Suspects:");
			for(Actor actor : this.suspects)
			{
				ConsoleHelper.button(out, actor.name, () -> this.talkTo(out, rand, actor));
			}
		}
		ConsoleHelper.newline(out);
		ConsoleHelper.divider(out, true);
	}

	private void evaluate(Console out, Random rand, Actor target)
	{
		out.clear();
		ConsoleHelper.message(out, target.name);
		ConsoleHelper.newline(out);
		{
			ConsoleHelper.message(out, target.toString());
		}
		ConsoleHelper.newline(out);
		ConsoleHelper.divider(out, true);
		ConsoleHelper.button(out, "Back", () -> main(out, rand));
	}

	private void talkTo(Console out, Random rand, Actor target)
	{
		out.clear();
		ConsoleHelper.message(out, target.name);
		ConsoleHelper.newline(out);
		{
			ConsoleHelper.message(out, "What do you want?");
			ConsoleHelper.button(out, "Evaluate", () -> this.evaluate(out, rand, target));
		}
		ConsoleHelper.newline(out);
		ConsoleHelper.divider(out, true);
		ConsoleHelper.button(out, "Back", () -> main(out, rand));
	}

	private void solveMystery(Console out)
	{
		ConsoleHelper.message(out, "The Solution");
		ConsoleHelper.newline(out);
		{
			ConsoleHelper.message(out, "The murderer is: " + this.murderer.name);
			ConsoleHelper.message(out, "The motive is: " + this.motive);
		}
		ConsoleHelper.newline(out);
		ConsoleHelper.divider(out, true);
	}
}
