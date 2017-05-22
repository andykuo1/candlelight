package org.bstone.input;

import org.bstone.util.listener.Listenable;
import org.bstone.window.Window;

/**
 * Created by Andy on 4/26/17.
 */
public class InputEngine
{
	public interface OnInputUpdateListener
	{
		void onInputUpdate(InputEngine inputEngine);
	}

	public final Listenable<OnInputUpdateListener> onInputUpdate = new Listenable<>((listener, objects) -> listener.onInputUpdate((InputEngine) objects[0]));

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

		this.onInputUpdate.notifyListeners(this);
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
