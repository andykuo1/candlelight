package org.zilar.console.board;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 10/7/17.
 */
public class Blackboard
{
	private final Map<String, Object> data = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <E> E get(String key)
	{
		return (E) this.data.get(key);
	}

	public void put(String key, Object value)
	{
		this.data.put(key, value);
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
