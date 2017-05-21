package org.bstone.input;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 4/17/17.
 */
public class InputManager
{
	public interface Listener
	{
		void onInputUpdate(InputManager inputManager);
	}

	private final Map<String, int[]> keymap = new HashMap<>();

	private final boolean[] keypress = new boolean[512];
	private final boolean[] keyrelease = new boolean[512];
	private final boolean[] keys = new boolean[512];

	private final boolean[] buttonpress = new boolean[3];
	private final boolean[] buttonrelease = new boolean[3];
	private final boolean[] buttons = new boolean[3];

	private final Vector2d mousemotion = new Vector2d();
	private final Vector2d mouseprev = new Vector2d();

	private final InputEngine inputEngine;

	private final Set<Listener> listeners = new HashSet<>();

	public InputManager(InputEngine inputEngine)
	{
		this.inputEngine = inputEngine;
		Keyboard.initialize(this);
		Mouse.initialize(this);
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
		for (int i = 0; i < this.keys.length; ++i)
		{
			boolean flag = this.inputEngine.isKeyDown(i);
			if (flag != this.keys[i])
			{
				if (flag)
				{
					if (!this.keypress[i])
					{
						this.keypress[i] = true;
					}
					else
					{
						this.keys[i] = true;
						this.keypress[i] = false;
					}
				}
				else
				{
					if (!this.keyrelease[i])
					{
						this.keyrelease[i] = true;
					}
					else
					{
						this.keys[i] = false;
						this.keyrelease[i] = false;
					}
				}
			}
		}

		for(int i = 0; i < this.buttons.length; ++i)
		{
			boolean flag = this.inputEngine.isMouseDown(i);
			if (flag != this.buttons[i])
			{
				if (flag)
				{
					if (this.buttonpress[i]) this.buttons[i] = true;
					this.buttonpress[i] = !this.buttonpress[i];
				}
				else
				{
					if (this.buttonrelease[i]) this.buttons[i] = false;
					this.buttonrelease[i] = !this.buttonrelease[i];
				}
			}
		}

		//TODO: This is a late update? but it is not...
		Vector2dc vec = this.inputEngine.getMousePosition();
		this.mousemotion.set(this.mouseprev);
		this.mousemotion.sub(vec);
		this.mouseprev.set(vec);

		for(Listener listener : this.listeners)
		{
			listener.onInputUpdate(this);
		}
	}

	public void registerKey(String key, int... keycodes)
	{
		int[] array = this.keymap.get(key);
		if (array != null)
		{
			int len = array.length;
			array = Arrays.copyOf(array, array.length + keycodes.length);
			for (int i = 0; i < keycodes.length; ++i)
			{
				array[i + len] = keycodes[i];
			}
		}
		else
		{
			array = keycodes;
		}

		this.keymap.put(key, array);
	}

	public boolean containsKey(String key)
	{
		return this.keymap.containsKey(key);
	}

	public boolean isKeyPressed(String key)
	{
		int[] keycodes = this.keymap.get(key);
		for (int keycode : keycodes)
		{
			if (this.keypress[keycode])
			{
				return true;
			}
		}
		return false;
	}

	public boolean isKeyReleased(String key)
	{
		int[] keycodes = this.keymap.get(key);
		for (int keycode : keycodes)
		{
			if (this.keyrelease[keycode])
			{
				return true;
			}
		}
		return false;
	}

	public boolean isKeyDown(String key)
	{
		int[] keycodes = this.keymap.get(key);
		for (int keycode : keycodes)
		{
			if (this.keys[keycode])
			{
				return true;
			}
		}
		return false;
	}

	public boolean isMousePressed(int button)
	{
		return this.buttonpress[button];
	}

	public boolean isMouseReleased(int button)
	{
		return this.buttonrelease[button];
	}

	public boolean isMouseDown(int button)
	{
		return this.buttons[button];
	}

	public Vector2dc getMousePosition()
	{
		return this.inputEngine.getMousePosition();
	}

	public Vector2dc getMouseMotion()
	{
		return this.mousemotion;
	}

	public Vector2dc getMouseScroll()
	{
		return this.inputEngine.getMouseScroll();
	}

	public InputEngine getInputEngine()
	{
		return this.inputEngine;
	}
}
