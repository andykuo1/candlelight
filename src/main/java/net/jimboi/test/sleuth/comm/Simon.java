package net.jimboi.test.sleuth.comm;

import net.jimboi.test.console.Console;
import net.jimboi.test.sleuth.SleuthStruct;

import java.util.Random;

/**
 * Created by Andy on 9/25/17.
 */
public class Simon extends SleuthStruct
{
	public String name;

	public ThinkMachine thinker;
	public StatMachine stats;

	public Environment environment;

	public int initiative = 0;

	public Simon(String name)
	{
		this.name = name;
		this.thinker = new ThinkMachine(this);
		this.stats = new StatMachine(this);
		this.environment = new Environment();
	}

	public void step(Console out, Random rand, Environment environment)
	{
		//Update / React to Environment

		//Start actions
		this.initiative = this.stats.getInitiative();
		while(this.initiative > 0)
		{
			ActionEvent event = this.thinker.think(out);
			event.execute(out, this, rand, environment);
			--this.initiative;
		}
	}
}
