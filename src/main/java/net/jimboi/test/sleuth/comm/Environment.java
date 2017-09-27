package net.jimboi.test.sleuth.comm;

import net.jimboi.test.console.Console;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andy on 9/23/17.
 */
public class Environment
{
	public Set<Simon> users = new HashSet<>();
	public Set<Item> items = new HashSet<>();

	public void set(Environment env)
	{
		this.users.clear();
		this.users.addAll(env.users);
		this.items.clear();
		this.items.addAll(env.items);
	}

	public void update(Console out, Random rand)
	{

	}
}
