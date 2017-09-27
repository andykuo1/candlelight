package net.jimboi.test.sleuth.cluedo;

import net.jimboi.test.console.Console;

import java.util.Random;

/**
 * Created by Andy on 9/24/17.
 */
public abstract class Cluedo
{
	public final Cluedo start(Console out, Random rand)
	{
		this.create(rand);
		this.play(out);
		return this;
	}

	protected abstract void create(Random rand);

	protected abstract void play(Console out);
}
