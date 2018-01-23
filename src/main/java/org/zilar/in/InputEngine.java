package org.zilar.in;

import org.bstone.kernel.Engine;
import org.zilar.in.adapter.InputAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 1/22/18.
 */
public class InputEngine implements Engine
{
	private final Map<String, InputContext> contexts = new HashMap<>();
	private final List<InputContext> contextQueue = new LinkedList<>();

	private final Map<String, InputAdapter> adapters = new HashMap<>();

	private final Set<InputProvider> providers = new HashSet<>();

	public InputEngine addProvider(InputProvider provider)
	{
		this.providers.add(provider);
		return this;
	}

	public void removeProvider(InputProvider provider)
	{
		this.providers.remove(provider);
	}

	@Override
	public boolean initialize()
	{
		return true;
	}

	@Override
	public void terminate()
	{
		this.contexts.clear();
		this.contextQueue.clear();

		this.adapters.clear();
	}

	@Override
	public void update()
	{
		for(InputProvider provider : this.providers)
		{
			InputState inputState = provider.getInputState();
			for (InputContext inputContext : this.contextQueue)
			{
				inputContext.poll(provider, inputState);
			}
		}
	}

	public void setContextPriority(String context, int priority)
	{
		InputContext inputContext = this.contexts.computeIfAbsent(context, (key) -> new InputContext(this));
		inputContext.setPriority(priority);
		for(int i = 0; i < this.contextQueue.size(); ++i)
		{
			if (inputContext.compareTo(this.contextQueue.get(i)) <= 0)
			{
				this.contextQueue.add(i, inputContext);
				return;
			}
		}
		this.contextQueue.add(inputContext);
	}

	protected InputContext getContext(String context)
	{
		InputContext inputContext = this.contexts.get(context);
		if (inputContext == null)
		{
			int priority = this.contextQueue.get(this.contextQueue.size()).getPriority() + 1;
			this.setContextPriority(context, priority);
			inputContext = this.contexts.get(context);
		}
		return inputContext;
	}

	public void registerInputAdapter(InputAdapter adapter)
	{
		this.adapters.put(adapter.getName(), adapter);
	}

	public InputAdapter getInputAdapter(String input)
	{
		return this.adapters.get(input);
	}

	public void registerState(String context, String intent, InputAdapter adapter)
	{
		this.registerInputAdapter(adapter);
		this.getContext(context).registerStateInput(intent);
	}

	public void registerAction(String context, String intent, InputAdapter adapter)
	{
		this.registerInputAdapter(adapter);
		this.getContext(context).registerActionInput(intent);
	}

	public void registerRange(String context, String intent, InputAdapter adapter)
	{
		this.registerInputAdapter(adapter);
		this.getContext(context).registerRangeInput(intent);
	}

	public Boolean getState(String context, String intent)
	{
		return this.getContext(context).getState(intent);
	}

	public Integer getAction(String context, String intent)
	{
		return this.getContext(context).getAction(intent);
	}

	public Float getRange(String context, String intent)
	{
		return this.getContext(context).getRange(intent);
	}
}
