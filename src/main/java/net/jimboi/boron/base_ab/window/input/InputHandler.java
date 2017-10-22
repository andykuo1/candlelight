package net.jimboi.boron.base_ab.window.input;

import net.jimboi.boron.base_ab.window.OldWindow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Andy on 5/20/17.
 */
public abstract class InputHandler
{
	protected final OldWindow window;
	protected final Set<Input> inputs = new HashSet<>();

	public InputHandler(OldWindow window)
	{
		this.window = window;
	}

	public abstract void update();

	public void poll()
	{
		for(Input input : this.inputs)
		{
			input.poll();
		}
	}

	public void add(Input input)
	{
		this.inputs.add(input);
	}

	public void remove(Input input)
	{
		this.inputs.remove(input);
	}

	public Input get(int id)
	{
		for(Input input : this.inputs)
		{
			if (input.getID() == id)
			{
				return input;
			}
		}

		return null;
	}

	public void clear()
	{
		this.inputs.clear();
	}

	public Iterator<Input> getInputs()
	{
		return this.inputs.iterator();
	}
}
