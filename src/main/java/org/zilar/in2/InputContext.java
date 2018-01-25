package org.zilar.in2;

import org.bstone.newinput.device.event.InputEvent;
import org.zilar.in2.adapter.InputAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Andy on 1/24/18.
 */
public class InputContext
{
	private final Map<String, InputAdapter> adapters = new LinkedHashMap<>();
	private boolean active = true;

	public InputContext setActive(boolean active)
	{
		this.active = active;
		return this;
	}

	public InputContext registerInput(String intent, InputAdapter adapter)
	{
		this.adapters.put(intent, adapter);
		return this;
	}

	public InputAdapter getInput(String intent)
	{
		return this.adapters.get(intent);
	}

	public boolean processEvent(InputEvent event, InputState dst)
	{
		if (this.active)
		{
			for (InputAdapter adapter : this.adapters.values())
			{
				if (adapter.processEvent(event, dst))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean isActive()
	{
		return this.active;
	}
}
