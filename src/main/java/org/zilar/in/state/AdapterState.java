package org.zilar.in.state;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 1/22/18.
 */
public class AdapterState
{
	private final Map<String, Boolean> states = new HashMap<>();
	private final Map<String, Integer> actions = new HashMap<>();
	private final Map<String, Float> ranges = new HashMap<>();

	public void setState(String intent, Boolean state)
	{
		this.states.put(intent, state);
	}

	public void setAction(String intent, Integer action)
	{
		this.actions.put(intent, action);
	}

	public void setRange(String intent, Float range)
	{
		this.ranges.put(intent, range);
	}

	public Boolean getState(String intent)
	{
		return this.states.get(intent);
	}

	public Integer getAction(String intent)
	{
		return this.actions.get(intent);
	}

	public Float getRange(String intent)
	{
		return this.ranges.get(intent);
	}
}
