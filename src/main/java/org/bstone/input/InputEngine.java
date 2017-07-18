package org.bstone.input;

import org.bstone.window.Window;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Andy on 4/26/17.
 */
public class InputEngine
{
	private final Set<InputLayer> layers = new TreeSet<>();

	private final Window window;

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

	public void update()
	{
		this.keyboard.poll();
		this.mouse.poll();
		this.joypad.poll();

		for(InputLayer layer : this.layers)
		{
			layer.onInputUpdate(this);
		}
	}

	public void addInputLayer(InputLayer layer)
	{
		this.layers.add(layer);
	}

	public void removeInputLayer(InputLayer layer)
	{
		this.layers.remove(layer);
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
