package net.jimboi.test.sleuth.queso;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 9/26/17.
 */
public class EstadoMachina
{
	private Set<String> states = new HashSet<>();
	private Map<Integer, Set<String>> temp = new HashMap<>();

	public boolean contains(String state)
	{
		if (this.states.contains(state)) return true;

		for(Set<String> states : this.temp.values())
		{
			if (states.contains(state))
			{
				return true;
			}
		}

		return false;
	}

	public void set(String state)
	{
		this.states.add(state);
	}

	public void add(int layer, String state)
	{
		this.temp.computeIfAbsent(layer, k -> new HashSet<>()).add(state);
	}

	public void reset(int layer)
	{
		Set<String> states = this.temp.get(layer);
		if (states != null) states.clear();
	}
}
