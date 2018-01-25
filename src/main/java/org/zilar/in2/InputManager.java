package org.zilar.in2;

import org.bstone.newinput.device.event.InputEvent;
import org.zilar.in2.adapter.InputActionAdapter;
import org.zilar.in2.adapter.InputRangeAdapter;
import org.zilar.in2.adapter.InputStateAdapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Andy on 1/23/18.
 */
public class InputManager
{
	private final Map<String, InputContext> contextMapping = new HashMap<>();
	private final PrioritizedList<InputContext> contexts = new PrioritizedList<>();

	private final InputState inputState = new InputState();

	public void process(Iterator<InputEvent> eventProvider)
	{
		while(eventProvider.hasNext())
		{
			InputEvent event = eventProvider.next();
			for(InputContext context : this.contexts)
			{
				if(context.processEvent(event, this.inputState))
				{
					eventProvider.remove();
					break;
				}
			}
		}
	}

	//Called every game loop before logic
	public void poll()
	{
		this.inputState.poll();
		//Always consume actions each loop
		//Let active until deactivate (dont consume)
		//Range is changed continuously (dont consume)

		//Action
		//Range
		//State

		//ButtonAction
		//ButtonState
		//VirtualButtonRange
		//AxisRange
		//VirtualAxisState
		//VirtualAxisAction
	}

	public void clear()
	{
		this.inputState.clear();
	}

	public InputManager registerAction(String context, String intent, InputActionAdapter adapter)
	{
		InputContext inputContext = this.getContext(context);
		inputContext.registerInput(intent, adapter);
		return this;
	}

	public InputManager registerState(String context, String intent, InputStateAdapter adapter)
	{
		InputContext inputContext = this.getContext(context);
		inputContext.registerInput(intent, adapter);
		return this;
	}

	public InputManager registerRange(String context, String intent, InputRangeAdapter adapter)
	{
		InputContext inputContext = this.getContext(context);
		inputContext.registerInput(intent, adapter);
		return this;
	}

	public boolean getAction(String context, String intent)
	{
		//Pressed, Released, Once
		return ((InputActionAdapter) this.getContext(context).getInput(intent)).getAction();
	}

	public boolean getState(String context, String intent)
	{
		//Down, Up, IsScrolling, Continuous
		return ((InputStateAdapter) this.getContext(context).getInput(intent)).getState();
	}

	public float getRange(String context, String intent)
	{
		//Joystick, Mouse, Vertical, Horizontal, Normalized
		return ((InputRangeAdapter) this.getContext(context).getInput(intent)).getRange();
	}

	public InputManager setContextPriority(String context, int priority)
	{
		InputContext inputContext = this.getContext(context);
		this.contexts.add(priority, inputContext);
		return this;
	}

	public InputManager setContextActive(String context, boolean active)
	{
		InputContext inputContext = this.getContext(context);
		inputContext.setActive(active);
		return this;
	}

	protected InputContext getContext(String context)
	{
		InputContext inputContext = this.contextMapping.computeIfAbsent(context, key -> new InputContext());
		this.contexts.add(inputContext);
		return inputContext;
	}

	public boolean isContextActive(String context)
	{
		return this.getContext(context).isActive();
	}

	public InputState getInputState()
	{
		return this.inputState;
	}
}
