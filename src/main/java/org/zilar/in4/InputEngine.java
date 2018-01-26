package org.zilar.in4;

import org.bstone.kernel.Engine;
import org.bstone.newinput.device.event.InputEvent;
import org.bstone.newinput.device.event.InputEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Andy on 1/24/18.
 */
public class InputEngine implements Engine
{
	private static final Logger LOG = LoggerFactory.getLogger(InputEngine.class);

	private final Queue<InputEvent> inputEvents = new ArrayBlockingQueue<>(100);
	private final Queue<InputEventListener> listeners = new ArrayDeque<>();

	private final Queue<InputHandler> handlers = new ArrayDeque<>();

	private final Map<String, InputMapper> contextMapping = new HashMap<>();
	private final Queue<InputMapper> contexts = new ArrayDeque<>();

	private InputState inputState;

	public InputEngine addEventListener(InputEventListener listener)
	{
		if (this.listeners.contains(listener)) throw new IllegalArgumentException("found duplicate event listener");
		this.listeners.add(listener);
		return this;
	}

	public InputEngine removeEventListener(InputEventListener listener)
	{
		if (!this.listeners.contains(listener)) throw new IllegalArgumentException("cannot find event listener");
		this.listeners.remove(listener);
		return this;
	}

	public void clearEventListeners()
	{
		this.listeners.clear();
	}

	public InputEngine addContext(String context)
	{
		if (this.contextMapping.containsKey(context)) throw new IllegalArgumentException("found duplicate context with name '" + context + "'");
		InputMapper mapper = new InputMapper();
		this.contextMapping.put(context, mapper);
		this.contexts.add(mapper);
		return this;
	}

	public InputEngine removeContext(String context)
	{
		if (!this.contextMapping.containsKey(context)) throw new IllegalArgumentException("cannot find context with name '" + context + "'");
		InputMapper mapper = this.contextMapping.remove(context);
		this.contexts.remove(mapper);
		return this;
	}

	public void clearContexts()
	{
		this.contextMapping.clear();
		this.contexts.clear();
	}

	public InputMapper getContext(String context)
	{
		if (!this.contextMapping.containsKey(context)) throw new IllegalArgumentException("cannot find context with name '" + context + "'");
		return this.contextMapping.get(context);
	}

	public InputEngine addInputHandler(InputHandler handler)
	{
		if (this.handlers.contains(handler)) throw new IllegalArgumentException("found duplicate input handler");
		this.handlers.add(handler);
		return this;
	}

	public InputEngine removeInputHandler(InputHandler handler)
	{
		if (!this.handlers.contains(handler)) throw new IllegalArgumentException("cannot find input handler");
		this.handlers.remove(handler);
		return this;
	}

	public void clearInputHandlers()
	{
		this.handlers.clear();
	}

	public InputEngine registerInput(String context, String intent, InputAdapter... adapters)
	{
		InputMapper ctx = this.getContext(context);
		for(InputAdapter adapter : adapters)
		{
			ctx.registerInput(intent, adapter);
		}
		return this;
	}

	@Override
	public boolean initialize()
	{
		this.inputState = new InputState();
		return true;
	}

	@Override
	public void update()
	{
		while(!this.inputEvents.isEmpty())
		{
			InputEvent event = this.inputEvents.poll();

			//Process event to intent
			boolean flag = false;
			for(InputMapper context : this.contexts)
			{
				if (context.processInput(event, this.inputState))
				{
					flag = true;
					break;
				}
			}

			//Process remaining events
			if (!flag)
			{
				for (InputEventListener listener : this.listeners)
				{
					if (listener.onInputEvent(event)) break;
				}
			}
		}

		//Process intents
		for(InputHandler handler : this.handlers)
		{
			handler.onInputUpdate(this.inputState);
		}
	}

	@Override
	public void terminate()
	{
		this.listeners.clear();
		this.contexts.clear();
		this.handlers.clear();
	}

	public boolean getState(String intent)
	{
		return this.inputState.getState(intent);
	}

	public boolean getAction(String intent)
	{
		return this.inputState.getAction(intent);
	}

	public float getRange(String intent)
	{
		return this.inputState.getRange(intent);
	}

	public void addToEventQueue(InputEvent evt)
	{
		this.inputEvents.offer(evt);
	}

	public InputEvent nextEvent()
	{
		return this.inputEvents.poll();
	}

	public boolean hasEvent()
	{
		return this.inputEvents.isEmpty();
	}
}
