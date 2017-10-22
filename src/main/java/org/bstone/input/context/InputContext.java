package org.bstone.input.context;

import org.bstone.input.InputListener;
import org.bstone.input.mapping.Input;
import org.bstone.util.Pair;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Andy on 10/13/17.
 */
public class InputContext
{
	private final InputMapping mapping;

	private final Set<String> events = new HashSet<>();

	private final Queue<Pair<Integer, InputListener>> listeners = new PriorityQueue<>(
			Comparator.comparingInt(Pair::getFirst));

	public InputContext(InputMapping mapping)
	{
		this.mapping = mapping;
	}

	public void poll()
	{
		for(Input input : this.mapping.getInputs())
		{
			if (input.isDirty())
			{
				this.fireInput(input);
			}
		}

		if (!this.events.isEmpty())
		{
			Iterator<Pair<Integer, InputListener>> iter = this.listeners.iterator();
			while (iter.hasNext())
			{
				Pair<Integer, InputListener> pair = iter.next();
				pair.getSecond().onInputUpdate(this);
			}
		}

		for(String event : this.events)
		{
			this.mapping.getInputMapping(event).clean();
		}

		this.events.clear();
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

	public void fireInput(Input input)
	{
		for(String name : this.mapping.getInputNames())
		{
			Input value = this.mapping.getInputMapping(name);
			if (value.equals(input))
			{
				this.events.add(name);
			}
		}
	}

	public void consumeInput(String name)
	{
		this.events.remove(name);
	}

	public void consumeInputs()
	{
		this.events.clear();
	}

	public Input getInput(String name)
	{
		if (this.events.contains(name))
		{
			return this.mapping.getInputMapping(name);
		}
		else
		{
			throw new IllegalArgumentException("could not find input with name '" + name + "'");
		}
	}

	public boolean hasInput(String name)
	{
		return this.events.contains(name);
	}

	public Iterable<String> getInputEvents()
	{
		return this.events;
	}

	public InputMapping getMapping()
	{
		return this.mapping;
	}
}
