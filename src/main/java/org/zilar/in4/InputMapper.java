package org.zilar.in4;

import org.bstone.newinput.device.event.InputEvent;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Andy on 1/26/18.
 */
public class InputMapper
{
	private final Map<InputAdapter, String> mapping = new HashMap<>();
	private final Queue<InputAdapter> adapters = new ArrayDeque<>();

	private boolean active = true;

	public void registerInput(String intent, InputAdapter adapter)
	{
		String s = this.mapping.get(adapter);
		if (s != null)
		{
			this.mapping.remove(adapter);
			this.adapters.remove(adapter);
		}
		this.mapping.put(adapter, intent);
		this.adapters.add(adapter);
	}

	//Called for every event to process to intent
	public boolean processInput(InputEvent event, InputState state)
	{
		for(InputAdapter adapter : this.adapters)
		{
			String intent = this.mapping.get(adapter);
			if (adapter.processInput(intent, event, state))
			{
				return true;
			}
		}
		return false;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public boolean isActive()
	{
		return this.active;
	}
}
