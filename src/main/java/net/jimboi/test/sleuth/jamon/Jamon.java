package net.jimboi.test.sleuth.jamon;

import net.jimboi.test.console.Console;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andy on 9/26/17.
 */
public class Jamon
{
	public JinkMachine thinker = new JinkMachine();
	public Set<Joal> goals = new HashSet<>();
	public boolean dead = false;

	public Accion nextAction;

	public void think(Console out, Random rand)
	{
		//Get all possible actions based off of known Environs
		Set<Accion> actions = new HashSet<>();
		//Evaluate them
		//All Actions have requirements (which then are included in the cost)
		//Possible requirements: Knife in Hand, Confidence, Person in sight, Morning Time,
		//Multiple passes, so to obtain certain requirements of likely actions, other actions are more important to do first.
		//All Actions have side effects (which also alter cost)
		//All Actions have an expected result.
		//If 2 actions have the same requirements and also share the same goal, then it is more efficient.
		//Goals favor a certain part of an Action (acts as a multiplier) based off of result.
		//Action costs are dependent on user status, skills and knowledge
		//Pick one to do.
		this.nextAction = this.thinker.evaluateAll(this, actions);
	}

	public void step(Console out, Random rand, Environs env)
	{
		//Do the one that was picked. In any way possible.
		//This may run multiple times after think.
	}
}
