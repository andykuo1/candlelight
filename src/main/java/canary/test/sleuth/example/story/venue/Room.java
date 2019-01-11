package canary.test.sleuth.example.story.venue;

import canary.test.sleuth.example.story.Item;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 10/8/17.
 */
public final class Room
{
	String name;

	public final Set<Item> items = new HashSet<>();

	Room() {}

	public Iterable<Item> getItems()
	{
		return this.items;
	}

	public final String getName()
	{
		return this.name;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
