package org.zilar.in;

import org.zilar.in.adapter.InputActionAdapter;
import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.adapter.InputStateAdapter;
import org.zilar.in.provider.InputProvider;

import java.util.HashSet;
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

	private final AdapterState adapterState = new AdapterState();

	private boolean active = true;
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

	public InputContext setActive(boolean active)
	{
		this.active = active;
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
		return this.active ? this.adapterState.getState(intent) : null;
	}

	public Integer getAction(String intent)
	{
		return this.active ? this.adapterState.getAction(intent) : null;
	}

	public Float getRange(String intent)
	{
		return this.active ? this.adapterState.getRange(intent) : null;
	}

	public void poll(InputProvider provider, InputState inputState)
	{
		if (!this.active) return;

		for(String intent : this.stateInputs)
		{
			InputStateAdapter adapter = (InputStateAdapter) this.inputEngine.getInputAdapter(intent);
			if (adapter == null) continue;
			Boolean state = adapter.getState(inputState);
			this.adapterState.setState(intent, state);
		}

		for(String intent : this.actionInputs)
		{
			InputActionAdapter adapter = (InputActionAdapter) this.inputEngine.getInputAdapter(intent);
			if (adapter == null) continue;
			Integer action = adapter.getAction(inputState);
			this.adapterState.setAction(intent, action);
		}

		for(String intent : this.rangeInputs)
		{
			InputRangeAdapter adapter = (InputRangeAdapter) this.inputEngine.getInputAdapter(intent);
			if (adapter == null) continue;
			Float range = adapter.getRange(inputState);
			this.adapterState.setRange(intent, range);
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

	public boolean isActive()
	{
		return this.active;
	}

	public InputEngine getInputEngine()
	{
		return this.inputEngine;
	}
}
