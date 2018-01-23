package org.zilar.in;

import org.zilar.in.adapter.InputAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 1/22/18.
 */
public class InputContext implements Comparable<InputContext>
{
	private final InputEngine inputEngine;

	private final Set<String> stateInputs = new HashSet<>();
	private final Set<String> actionInputs = new HashSet<>();
	private final Set<String> rangeInputs = new HashSet<>();

	private final Map<String, Boolean> states = new HashMap<>();
	private final Map<String, Integer> actions = new HashMap<>();
	private final Map<String, Float> ranges = new HashMap<>();

	private int priority = 0;

	public InputContext(InputEngine inputEngine)
	{
		this.inputEngine = inputEngine;
	}

	public InputContext setPriority(int priority)
	{
		this.priority = priority;
		return this;
	}

	public void registerStateInput(String intent)
	{
		this.stateInputs.add(intent);
	}

	public void registerActionInput(String intent)
	{
		this.actionInputs.add(intent);
	}

	public void registerRangeInput(String intent)
	{
		this.rangeInputs.add(intent);
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

	public void poll(InputProvider provider, InputState inputState)
	{
		for(String intent : this.stateInputs)
		{
			InputAdapter adapter = this.inputEngine.getInputAdapter(intent);
			if (adapter == null || !adapter.getProvider().equals(provider)) continue;
			Boolean state = adapter.getState(inputState);
			this.states.put(intent, state);
		}

		for(String intent : this.actionInputs)
		{
			InputAdapter adapter = this.inputEngine.getInputAdapter(intent);
			if (adapter == null || !adapter.getProvider().equals(provider)) continue;
			Integer action = adapter.getAction(inputState);
			this.actions.put(intent, action);
		}

		for(String intent : this.rangeInputs)
		{
			InputAdapter adapter = this.inputEngine.getInputAdapter(intent);
			if (adapter == null || !adapter.getProvider().equals(provider)) continue;
			Float range = adapter.getRange(inputState);
			this.ranges.put(intent, range);
		}
	}

	@Override
	public int compareTo(InputContext o)
	{
		int diff = this.priority - o.priority;
		if (diff == 0) return this.hashCode() - o.hashCode();
		return diff;
	}

	public int getPriority()
	{
		return this.priority;
	}

	public InputEngine getInputEngine()
	{
		return this.inputEngine;
	}
}
