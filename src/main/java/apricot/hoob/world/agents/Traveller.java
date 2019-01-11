package apricot.hoob.world.agents;

import apricot.hoob.world.World;

/**
 * Created by Andy on 7/13/17.
 */
public class Traveller extends MoveAgent
{
	public String name;

	//Hunger system

	//Sleep system
	// - Rest, Sleep
	//      - Rest : Will recover some stamina, but takes shorter
	//      - Sleep : Will recover lots of stamina, but takes longer
	// - Each rest / sleep at a campsite will require food (and if night, wood)

	//Combat system
	// - Available weapon types: Melee or Ranged
	//      - Melee : Will lock opponent in place, unless disengage
	//      - Ranged : Can shoot from a distance, but will not lock

	//Movement system
	// - Walk, Run, Sneak
	//      - Walk : Will slowly drain stamina
	//      - Run : Will drain stamina, but will move faster
	//      - Sneak : Will be harder to detect, but will move slower


	//Stats
	public float health;            //How much damage to take before Death
	public float healthRegen;       //How much health regenerated, when Regenerating
	public float stamina;           //How much exhaustion to take before Tired
	public float staminaRegen;      //How much stamina regenerated, when Regenerating

	public float moveSpeed;         //Normal movement speed
	public float moveExhaustion;    //Exhaustion take from moving
	public float runSpeed;          //Running movement speed
	public float runExhaustion;     //Exhaustion taken from running

	public float sightDist;         //Visible distance

	//Skills (can be negative)
	/*
	public int skillBow;
	public int skillSword;
	public int skillArmor;
	public int skillHorseriding;
	public int skillWandering;
	public int skillScouting;
	public int skillWoodGathering;
	public int skillFoodGathering;
	*/

	public Traveller(World world)
	{
		super(world);
	}

	@Override
	public float getMoveSpeed()
	{
		return this.moveSpeed;
	}
}
