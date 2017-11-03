package org.bstone.input;

import org.bstone.input.adapter.IAction;
import org.bstone.input.adapter.IRange;
import org.bstone.input.adapter.IState;
import org.bstone.input.adapter.InputAdapterAccumulator;
import org.bstone.input.adapter.InputAdapterMaximizer;
import org.bstone.input.event.AbstractInputEvent;
import org.bstone.input.event.ActionEvent;
import org.bstone.input.event.RangeEvent;
import org.bstone.input.event.StateEvent;
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

	private final InputEngine inputEngine;

	InputContext(InputEngine inputEngine)
	{
		this.inputEngine = inputEngine;
	}

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
		listener.onInputStart(this.inputEngine, this);
		return listener;
	}

	public void removeListener(InputListener listener)
	{
		this.listeners.removeIf(integerInputListenerPair -> {
			if (integerInputListenerPair.getSecond().equals(listener))
			{
				listener.onInputStop(this.inputEngine, this);
				return true;
			}
			return false;
		});
	}

	public void removeListeners()
	{
		Iterator<Pair<Integer, InputListener>> iter = this.listeners.iterator();
		while(iter.hasNext())
		{
			Pair<Integer, InputListener> pair = iter.next();
			pair.getSecond().onInputStop(this.inputEngine, this);
			iter.remove();
		}
	}

	public void registerEvent(String key, IState... states)
	{
		if (this.isEventRegistered(key)) throw new IllegalArgumentException("found duplicate event registered by key '" + key + "'");

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
		if (this.isEventRegistered(key)) throw new IllegalArgumentException("found duplicate event registered by key '" + key + "'");

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
		if (this.isEventRegistered(key)) throw new IllegalArgumentException("found duplicate event registered by key '" + key + "'");

		if (ranges.length == 1)
		{
			this.ranges.put(key, new RangeEvent(ranges[0]));
		}
		else
		{
			this.ranges.put(key, new RangeEvent(new InputAdapterAccumulator(ranges)));
		}
	}

	public void unregisterEvent(String key)
	{
		if (this.states.containsKey(key)) this.states.remove(key);
		else if (this.actions.containsKey(key)) this.actions.remove(key);
		else if (this.ranges.containsKey(key)) this.ranges.remove(key);

		throw new IllegalArgumentException("cannot find registered event with key '" + key + "'");
	}

	public void unregisterAll()
	{
		this.states.clear();
		this.actions.clear();
		this.ranges.clear();
	}

	public boolean isEventRegistered(String key)
	{
		return this.states.containsKey(key) || this.actions.containsKey(key) || this.ranges.containsKey(key);
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

	public final InputEngine getInputEngine()
	{
		return this.inputEngine;
	}
}
