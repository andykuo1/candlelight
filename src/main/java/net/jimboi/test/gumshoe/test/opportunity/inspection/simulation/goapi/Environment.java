package net.jimboi.test.gumshoe.test.opportunity.inspection.simulation.goapi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 12/9/17.
 */
public class Environment
{
	private final Map<String, Boolean> attributes = new HashMap<>();

	public void set(Environment env)
	{
		this.attributes.clear();
		this.attributes.putAll(env.attributes);
	}

	public boolean set(String id, boolean state)
	{
		Boolean b = this.attributes.get(id);
		this.attributes.put(id, state);
		return b == null || b != state;
	}

	public Boolean get(String id)
	{
		return this.attributes.get(id);
	}

	public boolean contains(String id)
	{
		return this.attributes.containsKey(id);
	}

	public Map<String, Boolean> getAttributes()
	{
		return this.attributes;
	}

	public int getEstimatedRemainingActionCost(Environment env)
	{
		int diff = 0;
		for(Map.Entry<String, Boolean> attribute : this.getAttributes().entrySet())
		{
			if (env.get(attribute.getKey()) != attribute.getValue())
			{
				diff++;
			}
		}
		return diff;
	}

	public boolean isSatisfiedActionState(Environment env)
	{
		for(Map.Entry<String, Boolean> attribute : this.getAttributes().entrySet())
		{
			if (env.get(attribute.getKey()) != attribute.getValue())
			{
				return false;
			}
		}
		return true;
	}
}
