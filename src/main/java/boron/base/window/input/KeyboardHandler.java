package boron.base.window.input;

import boron.base.window.OldWindow;

import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 5/20/17.
 */
@Deprecated
public class KeyboardHandler extends InputHandler
{
	protected final Set<TextHandler> textHandlers = new HashSet<>();

	protected KeyboardHandler(OldWindow window)
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
				if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_BACKSPACE) this.updateText(-1);

				this.updateKey(key, action);
			}
		});

		GLFW.glfwSetCharCallback(window.handle(), (handle, codepoint) -> this.updateText(codepoint));
	}

	@Override
	public void update()
	{
	}

	public void addTextHandler(TextHandler handler)
	{
		this.textHandlers.add(handler);
	}

	public void removeTextHandler(TextHandler handler)
	{
		this.textHandlers.remove(handler);
	}

	private void updateKey(int key, int action)
	{
		for(Input input : this.inputs)
		{
			final int id = input.getID();
			if (id == key || id == -1)
			{
				input.update(key, action == GLFW.GLFW_RELEASE ? 0 : 1);
				break;
			}
		}
	}

	private void updateText(int codepoint)
	{
		if (codepoint == -1)
		{
			for(TextHandler handler : this.textHandlers)
			{
				handler.backspace();
			}
		}
		else
		{
			for (TextHandler handler : this.textHandlers)
			{
				handler.text(codepoint);
			}
		}
	}
}
