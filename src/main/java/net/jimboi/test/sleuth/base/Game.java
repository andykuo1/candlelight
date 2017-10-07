package net.jimboi.test.sleuth.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 10/7/17.
 */
public class Game
{
	private final Map<String, Object> data = new HashMap<>();

	public void put(String key, Object value)
	{
		this.data.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key)
	{
		return (T) this.data.get(key);
	}

	public boolean contains(String key)
	{
		return this.data.containsKey(key);
	}

	public boolean isEmpty()
	{
		return this.data.isEmpty();
	}

	public void clear()
	{
		this.data.clear();
	}
}
