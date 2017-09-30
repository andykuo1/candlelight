package net.jimboi.test.sleuth.comm;

import java.util.Random;

/**
 * Created by Andy on 9/27/17.
 */
public class WorldMurder extends World
{
	@Override
	public void run(Random rand)
	{
		/*

		All Objects have Properties and Actions change those Properties.
		Goals are desired Properties on certain Objects

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

		Goals:
			- Do what you need to do
				- Look for the ITEM in question, if cannot see it
					- Look for CONTAINERS that can contain such an ITEM
					- Search those CONTAINERS (do the easy ones first, due to time bias)
			- Then erase the crime:
				- Put everything you can back
					- From what the person can remember (chance vs mentality)
				- Or hide them (shattered glass)
					- May try to hide the object in where it belongs

		Possible Actions:
			- Pick Up ITEM
				- REQ: can see ITEM
				- REQ: position = ITEM position
				- REQ: strength > weight class
				- REQ: if too big: use both hands
				- REQ: in hand = NONE
				- EFF: in hand = ITEM
			- Put ITEM in INVENTORY
				- REQ: in hand = ITEM
				- REQ: ITEM size < remaining space in INVENTORY
				- EFF: ITEM is in INVENTORY (and no longer visible)
				- EFF: in hand = NONE
			- Put ITEM in CONTAINER
				- REQ: in hand = ITEM
				- REQ: CONTAINER is OPENED
				- REQ: CONTAINER is NOT BLOCKED
				- REQ: position = CONTAINER position
				- REQ: ITEM size < remaining space in CONTAINER
				- EFF: ITEM is in CONTAINER (and no longer visible)
				- EFF: in hand = NONE
			- Drop ITEM (will drop item on ground)
				- REQ: in hand = ITEM
				- EFF: in hand = NONE
			- Move ITEM to CONTAINER (to BLOCK it)
				- REQ: in hand = ITEM
				- REQ: enough stamina
				- REQ: can move to
				- EFF: ITEM position = CONTAINER position
				- EFF: CONTAINER is BLOCKED
			- Move ITEM away from CONTAINER (to remove BLOCKED)
				- REQ: in hand = ITEM
				- REQ: enough stamina
				- REQ: can move to
				- EFF: ITEM position = moved to position
				- EFF: CONTAINER is NOT BLOCKED
			- Inspect ITEM
				- REQ: can see ITEM
				- EFF: get ITEM info (type, attributes, etc.)
			- Closely Inspect ITEM
				- REQ: in hand = ITEM
				- EFF: get all ITEM info (all attributes)
			- Open ENTRANCE ITEM
				- REQ: position = ITEM position
				- REQ: ENTRANCE is NOT BLOCKED
				- REQ: ENTRANCE is NOT LOCKED
				- EFF: ENTRANCE is OPENED
			- Close ENTRANCE ITEM
				- REQ: position = ITEM position
				- REQ: ENTRANCE is NOT BLOCKED
				- EFF: ENTRANCE is CLOSED
			- Open CONTAINER ITEM
				- REQ: ITEM has CONTAINER
				- REQ: ITEM is NOT LOCKED
				- REQ: position = ITEM position
				- REQ: ITEM is NOT BLOCKED
				- EFF: ITEM is OPENED
			- Close CONTAINER ITEM
				- REQ: ITEM has CONTAINER
				- REQ: position = ITEM position
				- REQ: ITEM is NOT BLOCKED
				- EFF: ITEM is CLOSED
			- Break ITEM
				- REQ: position = ITEM position
				- REQ: has enough stamina
				- EFF: ITEM is continually damaged by in-hand ITEM damage, until broken (or tired)
			- Hide in CONTAINER ITEM
				- REQ: CONTAINER is at least TALL or LARGE
				- REQ: CONTAINER is OPENED
				- REQ: CONTAINER is NOT BLOCKED
				- REQ: position = ITEM position
				- EFF: person is NOT VISIBLE
				- EFF: CONTAINER is CLOSED
			- Rest on RESTABLE ITEM
				- REQ: position = ITEM position
				- EFF: stamina restored (if sequential, heals more)
			- Stand and rest
				- EFF: stamina slightly restored (if sequential, heals more)


		Objects in the Living Room:
			- Couch
				- WEIGHT_HEAVY (strength to move)
				- RESTABLE
				- MATERIAL_CLOTH (determines flammability)
					- FLAMMABILITY
					- HARDNESS_SOFT (on hit will NOT hurt)
			- Glass Table
				- WEIGHT_HEAVY
				- MATERIAL_GLASS (determines transparency)
					- TRANSPARENCY
					- DURABILITY_LOW (easily destroyed)
			- End Table
				- WEIGHT_HEAVY
				- CONTAINER_SMALL (not visible, but can be opened)
					- Magazine
						- MATERIAL_PAPER
							- FLAMMABILITY
					- Watch
						- MATERIAL_METAL
						- VALUABLE_HIGH
						- GOLD
						- SIZE_TINY
						- WEIGHT_NONE
					- Gun
						- WEIGHT_LIGHT
						- SIZE_SMALL
						- MATERIAL_METAL
						- FIREARM
			- Vase of Flowers
				- SIZE_MEDIUM (determines can carry)
				- WEIGHT_LIGHT
				- MATERIAL_CERAMIC (determines hardness)
					- HARDNESS_HARD (on hit WILL hurt)
					- DURABILITY_LOW
				- CONTAINER_SMALL (drop flowers if opened)
					- Knife
						- WEIGHT_LIGHT
						- SIZE_SMALL
						- MATERIAL_METAL
						- SHARP
			- Radio
				- SIZE_MEDIUM (determines can carry)
				- WEIGHT_MEDIUM (decent to move)
			- Table Lamp
				- SIZE_MEDIUM (determines can carry)
				- WEIGHT_LIGHT (easy to move)
				- MATERIAL_METAL (will remove light bulb if used to hit)
					- DURABILITY_HIGH (not easy to break)
			- Picture Frame
				- SIZE_SMALL (determines can hide)
				- WEIGHT_LIGHT (easy to move)
				- MATERIAL_GLASS (on destroy will drop glass)
				- HARDNESS_LOW (on hit MAY hurt) (override material attribute)
			- Rug
				- MATERIAL_CLOTH
					- FLAMMABILITY (catch fire easily)
			- Pillow
				- SIZE_MEDIUM (determines can carry)
				- WEIGHT_LIGHT (easy to move)
				- MATERIAL_CLOTH (determines hardness)
					- FLAMMABILITY
					- HARDNESS_SOFT (on hit will NOT hurt)
			- Window
				- ENTRANCE
				- LOCKED (can be unlocked)
				- MATERIAL_GLASS (on destroy will drop glass in direction)
					- HARDNESS_HARD
					- DURABILITY_LOW
					- TRANSPARENCY
			- Curtains
				- CONTAINER_TALL (can hide a person)
				- MATERIAL_CLOTH
			- Door
				- ENTRANCE
				- MATERIAL_WOOD
				- LOCKED
				- DURABILITY_HIGH
			- Backdoor
				- ENTRANCE
				- MATERIAL_WOOD
				- LOCKED
				- DURABILITY_HIGH
		*/

		boolean running = true;
		while(running)
		{
			//if (this.containsAttribute("LEAVE"))
			{
				running = false;
			}
		}
		System.exit(0);
	}
}
