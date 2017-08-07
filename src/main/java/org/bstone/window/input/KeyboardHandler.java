package org.bstone.window.input;

import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 5/20/17.
 */
public class KeyboardHandler extends InputHandler
{
	protected KeyboardHandler(Window window)
	{
		super(window);

		GLFW.glfwSetKeyCallback(window.handle(), (handle, key, scancode, action, mods) ->
		{
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
			{
				GLFW.glfwSetWindowShouldClose(handle, true);
			}
			else
			{
				this.updateKey(key, action);
			}
		});
	}

	@Override
	public void update()
	{
	}

	private void updateKey(int key, int action)
	{
		for(Input input : this.inputs)
		{
			if (input.getID() == key)
			{
				input.update(action == GLFW.GLFW_RELEASE ? 0 : 1);
				break;
			}
		}
	}
}
