package org.bstone.input;

import org.bstone.window.Window;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 4/26/17.
 */
public class InputEngine
{
	public interface Listener
	{
		void onInputUpdate(InputEngine inputEngine);
	}

	private final Window window;
	private final Set<Listener> listeners = new HashSet<>();

	private final KeyboardHandler keyboard;
	private final MouseHandler mouse;
	private final JoypadHandler joypad;

	public InputEngine(Window window)
	{
		this.window = window;

		this.keyboard = new KeyboardHandler(this);
		this.mouse = new MouseHandler(this);
		this.joypad = new JoypadHandler(this);

		InputManager.init(this);
	}

	public void addListener(Listener listener)
	{
		this.listeners.add(listener);
	}

	public void removeListener(Listener listener)
	{
		this.listeners.remove(listener);
	}

	public void update()
	{
		this.keyboard.poll();
		this.mouse.poll();
		this.joypad.poll();

		for(Listener listener : this.listeners)
		{
			listener.onInputUpdate(this);
		}
	}

	public KeyboardHandler getKeyboard()
	{
		return this.keyboard;
	}

	public MouseHandler getMouse()
	{
		return this.mouse;
	}

	public JoypadHandler getJoypad()
	{
		return this.joypad;
	}

	public Window getWindow()
	{
		return this.window;
	}
}
