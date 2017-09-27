package net.jimboi.test.sleuth.jamon;

import net.jimboi.test.console.Console;
import net.jimboi.test.console.ConsoleHelper;
import net.jimboi.test.sleuth.cluedo.Cluedo;

import java.util.Random;

/**
 * Created by Andy on 9/26/17.
 */
public class JamonSimulator extends Cluedo
{
	@Override
	protected void create(Random rand)
	{
		//This is a simulator, not a game. :)
	}

	@Override
	protected void play(Console out)
	{
		//Simulate!
		ConsoleHelper.message(out, "Starting simulation...");
		{
			//Premise: The murderer wants to kill the victim in the room.
			Random rand = new Random();

			Jamon murderer = new Jamon();
			Jamon victim = new Jamon();

			Joal killGoal = new Joal();
			murderer.goals.add(killGoal);

			Environs env = new Environs();

			while(!victim.dead)
			{
				murderer.think(out, rand);
				murderer.step(out, rand, env);

				victim.think(out, rand);
				victim.step(out, rand, env);
			}
		}
		ConsoleHelper.message(out, "Stopping simulation...");
	}
}
