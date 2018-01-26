package org.bstone.input.device.event;

import org.bstone.input.device.InputDevice;

/**
 * Created by Andy on 1/24/18.
 */
public class InputEvent
{
	public static ButtonEvent getButtonEvent(InputDevice src, int id, int state)
	{
		ButtonEvent evt = new ButtonEvent(src, id);
		evt.state = state;
		return evt;
	}

	public static ButtonEvent getButtonReleaseEvent(InputDevice src, int id)
	{
		return getButtonEvent(src, id, 0);
	}

	public static ButtonEvent getButtonPressEvent(InputDevice src, int id)
	{
		return getButtonEvent(src, id, 1);
	}

	public static TextEvent getTextEvent(InputDevice src, int id, int codepoint)
	{
		TextEvent evt = new TextEvent(src, id);
		evt.codepoint = codepoint;
		return evt;
	}

	public static AxisEvent getAxisEvent(InputDevice src, int id, float value)
	{
		AxisEvent evt = new AxisEvent(src, id);
		evt.value = value;
		return evt;
	}

	public InputDevice src;
	public int id;

	InputEvent(InputDevice src, int id)
	{
		this.src = src;
		this.id = id;
	}

	public String getEventID()
	{
		return this.src.getName() + "@" + this.id;
	}
}
