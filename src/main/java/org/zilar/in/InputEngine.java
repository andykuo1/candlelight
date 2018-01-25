package org.zilar.in;

import org.bstone.kernel.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zilar.in.adapter.InputAdapter;
import org.zilar.in.provider.InputProvider;
import org.zilar.in.state.InputState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Andy on 1/22/18.
 */
public class InputEngine implements Engine
{
	private static final Logger LOG = LoggerFactory.getLogger(InputEngine.class);

	private final Map<String, InputContext> contexts = new HashMap<>();
	private final List<InputContext> contextQueue = new LinkedList<>();

	private final Map<String, InputAdapter> adapters = new HashMap<>();

	private final InputState inputState = new InputState();

	public InputEngine addProvider(InputProvider provider)
	{
		this.inputState.addProvider(provider);
		return this;
	}

	public InputEngine removeProvider(InputProvider provider)
	{
		this.inputState.removeProvider(provider);
		return this;
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
		for (InputContext inputContext : this.contextQueue)
		{
			inputContext.poll(this.inputState);
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
			int priority = this.contextQueue.isEmpty() ? 0 :
					this.contextQueue.get(this.contextQueue.size() - 1).getPriority() + 1;
			this.setContextPriority(context, priority);
			inputContext = this.contexts.get(context);
		}
		return inputContext;
	}

	public InputAdapter register(String context, String intent, InputAdapter adapter)
	{
		this.adapters.put(intent, adapter);
		this.getContext(context).registerInput(intent);
		return adapter;
	}

	public InputAdapter getInputAdapter(String input)
	{
		return this.adapters.get(input);
	}

	public Boolean getState(String context, String intent)
	{
		return this.getContext(context).getState(intent);
	}

	public boolean getStateOrDefault(String context, String intent, boolean defaultValue)
	{
		Boolean b = this.getState(context, intent);
		return b != null ? b : defaultValue;
	}

	public Integer getAction(String context, String intent)
	{
		return this.getContext(context).getAction(intent);
	}

	public int getActionOrDefault(String context, String intent, int defaultValue)
	{
		Integer i = this.getAction(context, intent);
		return i != null ? i : defaultValue;
	}

	public Float getRange(String context, String intent)
	{
		return this.getContext(context).getRange(intent);
	}

	public float getRangeOrDefault(String context, String intent, float defaultValue)
	{
		Float f = this.getRange(context, intent);
		return f != null ? f : defaultValue;
	}
}
