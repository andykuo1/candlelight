package net.jimboi.test.sleuth.comm;

import net.jimboi.test.console.Console;
import net.jimboi.test.sleuth.cluedo.Cluedo;
import net.jimboi.test.sleuth.comm.wood.WorldWood;

import java.util.Random;

/**
 * Created by Andy on 9/25/17.
 */
public class CommSimulator extends Cluedo
{
	public static void main(String[] args)
	{
		CommSimulator simulator = new CommSimulator();
		simulator.start(null, new Random());
	}
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

	@Override
	protected void create(Random rand)
	{
		//This is a simulator, not a game. :)
	}

	@Override
	protected void play(Console out)
	{
		final Random rand = new Random();

		//Generate world
		WorldWood world = new WorldWood();
		world.run(rand);

		//When does the simulation end?
		System.exit(0);
	}
}
