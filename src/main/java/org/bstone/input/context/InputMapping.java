package org.bstone.input.context;

import org.bstone.input.InputEngine;
import org.bstone.input.mapping.Input;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 10/18/17.
 */
public class InputMapping
{
	protected final InputEngine inputEngine;

	protected final Map<String, Input> mapping = new HashMap<>();

	public InputMapping(InputEngine inputEngine)
	{
		this.inputEngine = inputEngine;
	}

	public void registerInputMapping(String name, Input input)
	{
		this.mapping.put(name, input);
	}

	public void deleteInputMapping(String name)
	{
		this.mapping.remove(name);
	}

	public void deleteInputs()
	{
		this.mapping.clear();
	}

	public Input getInputMapping(String name)
	{
		return this.mapping.get(name);
	}

	public boolean isInputDown(String name)
	{
		return this.mapping.get(name).getState();
	}

	public int getInputAction(String name)
	{
		return this.mapping.get(name).getAction();
	}

	public float getInputRange(String name)
	{
		return this.mapping.get(name).getRange();
	}

	public Iterable<String> getInputNames()
	{
		return this.mapping.keySet();
	}

	public Iterable<Input> getInputs()
	{
		return this.mapping.values();
	}

	public final InputEngine getInputEngine()
	{
		return this.inputEngine;
	}
}
