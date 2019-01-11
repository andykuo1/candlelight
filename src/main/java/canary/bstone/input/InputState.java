package canary.bstone.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 1/26/18.
 */
public class InputState
{
	private final Map<String, Boolean> actions = new HashMap<>();
	private final Map<String, Boolean> states = new HashMap<>();
	private final Map<String, Float> ranges = new HashMap<>();

	public void set(InputState state)
	{
		this.actions.putAll(state.actions);
		this.states.putAll(state.states);
		this.ranges.putAll(state.ranges);
	}

	public void setAction(String intent, boolean action)
	{
		this.actions.put(intent, action);
	}

	public void setState(String intent, boolean state)
	{
		this.states.put(intent, state);
	}

	public void setRange(String intent, float range)
	{
		this.ranges.put(intent, range);
	}

	public boolean getAction(String intent)
	{
		return this.getAction(intent, true);
	}

	public boolean getAction(String intent, boolean consume)
	{
		Boolean result = this.actions.get(intent);
		if (result == null) return false;
		if (consume && result) this.actions.put(intent, false);
		return result;
	}

	public boolean getState(String intent)
	{
		Boolean result = this.states.get(intent);
		return result != null && result;
	}

	public float getRange(String intent)
	{
		Float result = this.ranges.get(intent);
		return result != null ? result : 0F;
	}

	public Iterable<String> getActions()
	{
		return this.actions.keySet();
	}

	public Iterable<String> getStates()
	{
		return this.states.keySet();
	}

	public Iterable<String> getRanges()
	{
		return this.ranges.keySet();
	}

	public void clear()
	{
		this.actions.clear();
		this.states.clear();
		this.ranges.clear();
	}
}
