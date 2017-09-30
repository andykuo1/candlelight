package net.jimboi.test.sleuth.comm.wood;

import net.jimboi.test.sleuth.comm.World;

import java.util.Random;

/**
 * Created by Andy on 9/27/17.
 */
public class WorldWood extends World
{
	@Override
	public void run(Random rand)
	{
		AgentWood simon = new AgentWood();

		boolean running = true;

		//Run simulation
		while(running)
		{
			System.out.println("...next step...");
			//Update the users
			simon.step(this, rand);

			System.out.println("...update world...");

			//Should exit?
			if (this.containsAttribute(AgentWood.exitGame))
			{
				System.out.println(this);
				running = false;
			}
		}
	}
}
