package net.jimboi.test.sleuth.comm;

import net.jimboi.test.sleuth.comm.goap.MutableEnvironment;

import java.util.Random;

/**
 * Created by Andy on 9/27/17.
 */
public abstract class World extends MutableEnvironment
{
	public abstract void run(Random rand);
}
