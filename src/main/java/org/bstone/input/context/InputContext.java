package org.bstone.input.context;

import org.bstone.input.context.adapter.IAction;
import org.bstone.input.context.adapter.IRange;
import org.bstone.input.context.adapter.IState;
import org.bstone.input.context.adapter.InputAdapterAccumulator;
import org.bstone.input.context.adapter.InputAdapterMaximizer;
import org.bstone.input.context.event.AbstractInputEvent;
import org.bstone.input.context.event.ActionEvent;
import org.bstone.input.context.event.RangeEvent;
import org.bstone.input.context.event.StateEvent;
import org.bstone.util.Pair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * Created by Andy on 10/29/17.
 */
public class InputContext
{
	private final Map<String, StateEvent> states = new HashMap<>();
	private final Map<String, ActionEvent> actions = new HashMap<>();
	private final Map<String, RangeEvent> ranges = new HashMap<>();

	private final Queue<Pair<Integer, InputListener>> listeners = new PriorityQueue<>(
			Comparator.comparingInt(Pair::getFirst));

	public void poll()
	{
		this.getInputEvents().forEach(AbstractInputEvent::poll);

		Iterator<Pair<Integer, InputListener>> iter = this.listeners.iterator();
		while (iter.hasNext())
		{
			Pair<Integer, InputListener> pair = iter.next();
			pair.getSecond().onInputUpdate(this);
		}
	}

	public InputListener addListener(int priority, InputListener listener)
	{
		this.listeners.add(new Pair<>(priority, listener));
		return listener;
	}

	public void removeListener(InputListener listener)
	{
		this.listeners.removeIf(integerInputListenerPair -> integerInputListenerPair.getSecond().equals(listener));
	}

	public void removeListeners()
	{
		this.listeners.clear();
	}

	public void registerEvent(String key, IState... states)
	{
		if (states.length == 1)
		{
			this.states.put(key, new StateEvent(states[0]));
		}
		else
		{
			this.states.put(key, new StateEvent(new InputAdapterMaximizer<>(states)));
		}
	}

	public void registerEvent(String key, IAction... actions)
	{
		if (actions.length == 1)
		{
			this.actions.put(key, new ActionEvent(actions[0]));
		}
		else
		{
			this.actions.put(key, new ActionEvent(new InputAdapterMaximizer<>(actions)));
		}
	}

	public void registerEvent(String key, IRange... ranges)
	{
		if (ranges.length == 1)
		{
			this.ranges.put(key, new RangeEvent(ranges[0]));
		}
		else
		{
			this.ranges.put(key, new RangeEvent(new InputAdapterAccumulator(ranges)));
		}
	}

	public void unregisterState(String key)
	{
		this.states.remove(key);
	}

	public void unregisterAction(String key)
	{
		this.actions.remove(key);
	}

	public void unregisterRange(String key)
	{
		this.ranges.remove(key);
	}

	public void unregisterAll()
	{
		this.states.clear();
		this.actions.clear();
		this.ranges.clear();
	}

	public StateEvent getState(String key)
	{
		if (!this.states.containsKey(key))
		{
			throw new IllegalArgumentException("could not find input state event with key '" + key + "' in this context");
		}

		return this.states.get(key);
	}

	public ActionEvent getAction(String key)
	{
		if (!this.actions.containsKey(key))
		{
			throw new IllegalArgumentException("could not find input action event with key '" + key + "' in this context");
		}

		return this.actions.get(key);
	}

	public RangeEvent getRange(String key)
	{
		if (!this.ranges.containsKey(key))
		{
			throw new IllegalArgumentException("could not find input range event with key '" + key + "' in this context");
		}

		return this.ranges.get(key);
	}

	public final Stream<AbstractInputEvent> getInputEvents()
	{
		return Stream.concat(this.states.values().stream(),
				Stream.concat(this.actions.values().stream(),
						this.ranges.values().stream()));
	}
}
