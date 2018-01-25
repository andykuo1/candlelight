package org.zilar.in;

import org.zilar.in.adapter.InputActionAdapter;
import org.zilar.in.adapter.InputAdapter;
import org.zilar.in.adapter.InputRangeAdapter;
import org.zilar.in.adapter.InputStateAdapter;
import org.zilar.in.state.InputState;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 1/22/18.
 */
public class InputContext implements Comparable<InputContext>
{
	private final InputEngine inputEngine;

	private final Set<String> inputIntents = new HashSet<>();
	//private final Set<String> stateInputs = new HashSet<>();
	//private final Set<String> actionInputs = new HashSet<>();
	//private final Set<String> rangeInputs = new HashSet<>();

	//private final AdapterState adapterState = new AdapterState();

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

	public void registerInput(String intent)
	{
		this.inputIntents.add(intent);
	}

	public Boolean getState(String intent)
	{
		return this.active && this.inputIntents.contains(intent) ? ((InputStateAdapter) this.inputEngine.getInputAdapter(intent)).getState() : null;
	}

	public Integer getAction(String intent)
	{
		return this.active && this.inputIntents.contains(intent) ? ((InputActionAdapter) this.inputEngine.getInputAdapter(intent)).getAction() : null;
	}

	public Float getRange(String intent)
	{
		return this.active && this.inputIntents.contains(intent) ? ((InputRangeAdapter) this.inputEngine.getInputAdapter(intent)).getRange() : null;
	}

	public void poll(InputState inputState)
	{
		if (!this.active) return;

		for(String intent : this.inputIntents)
		{
			InputAdapter adapter = this.inputEngine.getInputAdapter(intent);
			if (adapter == null) continue;
			adapter.update(inputState);
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
