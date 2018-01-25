package org.zilar.in.provider;

import org.bstone.newinput.device.Keyboard;
import org.bstone.newinput.device.event.ButtonEvent;
import org.bstone.newinput.device.event.InputEvent;
import org.bstone.newinput.device.event.InputEventListener;
import org.bstone.newinput.device.event.TextEvent;
import org.lwjgl.glfw.GLFW;
import org.zilar.in.state.RawInputState;

/**
 * Created by Andy on 1/24/18.
 */
public class KeyboardHandler implements InputEventListener, InputProvider
{
	private final RawInputState providerState;
	private final StringBuffer charBuffer = new StringBuffer();

	public KeyboardHandler()
	{
		this.providerState = new RawInputState(this, 0, GLFW.GLFW_KEY_LAST + 1);
	}

	@Override
	public boolean onInputEvent(InputEvent evt)
	{
		if (evt.src instanceof Keyboard)
		{
			if (evt instanceof TextEvent)
			{
				if (evt.id == Keyboard.TEXT_CHAR)
				{
					this.charBuffer.append((char) ((TextEvent) evt).codepoint);
					return true;
				}
			}
			else if (evt instanceof ButtonEvent)
			{
				switch (((ButtonEvent) evt).state)
				{
					case ButtonEvent.STATE_RELEASE:
						this.providerState.releaseButton(evt.id);
						break;
					case ButtonEvent.STATE_PRESS:
						this.providerState.pressButton(evt.id);
						break;
					default:
						return false;
				}
				return true;
			}
		}
		return false;
	}

	public StringBuffer getCharBuffer()
	{
		return this.charBuffer;
	}

	@Override
	public RawInputState getProviderState()
	{
		return this.providerState;
	}
}
