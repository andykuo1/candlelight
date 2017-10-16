package org.bstone.input.context;

import org.bstone.input.InputEngine;
import org.bstone.input.InputListener;
import org.bstone.util.Pair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Andy on 10/13/17.
 */
public class InputContext
{
	private final InputEngine inputEngine;

	private final Map<String, Input> inputs = new HashMap<>();
	private final Set<String> currents = new HashSet<>();

	private final Queue<Pair<Integer, InputListener>> listeners = new PriorityQueue<>(
			Comparator.comparingInt(Pair::getFirst));

	public InputContext(InputEngine inputEngine)
	{
		this.inputEngine = inputEngine;
	}

	public void poll()
	{
		for(Input input : this.inputs.values())
		{
			if (input.isDirty())
			{
				this.fireInput(input);
				input.clean();
			}
		}

		Iterator<Pair<Integer, InputListener>> iter = this.listeners.iterator();
		while(iter.hasNext())
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

	public void clearListeners()
	{
		this.listeners.clear();
	}

	public void registerInputMapping(String name, Input input)
	{
		this.inputs.put(name, input);
	}

	public void deleteInputMapping(String name)
	{
		this.inputs.remove(name);
	}

	public Input getInputMapping(String name)
	{
		return this.inputs.get(name);
	}

	public void clearInputMappings()
	{
		this.consumeAll();
		this.inputs.clear();
	}

	public void fireInput(Input input)
	{
		for(Map.Entry<String, Input> entry : this.inputs.entrySet())
		{
			String name = entry.getKey();
			Input value = entry.getValue();
			if (value.equals(input))
			{
				this.currents.add(name);
			}
		}
	}

	public void consumeInput(String name)
	{
		this.currents.remove(name);
	}

	public void consumeAll()
	{
		this.currents.clear();
	}

	public IAction getInputAction(String name)
	{
		if (this.currents.contains(name))
		{
			return (IAction) this.inputs.get(name);
		}
		else
		{
			return null;
		}
	}

	public IRange getInputRange(String name)
	{
		if (this.currents.contains(name))
		{
			return (IRange) this.inputs.get(name);
		}
		else
		{
			return null;
		}
	}

	public IState getInputState(String name)
	{
		if (this.currents.contains(name))
		{
			return (IState) this.inputs.get(name);
		}
		else
		{
			return null;
		}
	}

	public InputEngine getInputEngine()
	{
		return this.inputEngine;
	}
}
