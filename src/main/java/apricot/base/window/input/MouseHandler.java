package apricot.base.window.input;

import apricot.base.window.OldWindow;

import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 5/20/17.
 */
@Deprecated
public class MouseHandler extends InputHandler
{
	public static final int POS_X = 8;
	public static final int POS_Y = 9;
	public static final int SCROLL_X = 10;
	public static final int SCROLL_Y = 11;

	private Input inputPosX;
	private Input inputPosY;
	private Input inputScrollX;
	private Input inputScrollY;

	private boolean locked;

	MouseHandler(OldWindow window)
	{
		super(window);

		GLFW.glfwSetMouseButtonCallback(window.handle(), (handle, button, action, mods)->
		{
			this.updateMouse(button, action);
		});

		GLFW.glfwSetCursorPosCallback(window.handle(), (handle, xpos, ypos) ->
		{
			if (this.inputPosX != null) this.inputPosX.update(POS_X, (float) xpos);
			if (this.inputPosY != null) this.inputPosY.update(POS_Y, (float) ypos);
		});

		GLFW.glfwSetScrollCallback(window.handle(), (handle, xoffset, yoffset)->
		{
			if (this.inputScrollX != null) this.inputScrollX.update(SCROLL_X, (float) xoffset);
			if (this.inputScrollY != null) this.inputScrollY.update(SCROLL_Y, (float) yoffset);
		});
	}

	@Override
	public void update()
	{

	}

	@Override
	public void add(Input input)
	{
		switch (input.getID())
		{
			case POS_X:
				this.inputPosX = input;
				break;
			case POS_Y:
				this.inputPosY = input;
				break;
			case SCROLL_X:
				this.inputScrollX = input;
				break;
			case SCROLL_Y:
				this.inputScrollY = input;
				break;
		}

		super.add(input);
	}

	private void updateMouse(int button, int action)
	{
		for(Input input : this.inputs)
		{
			if (input.getID() == button)
			{
				input.update(button, action == GLFW.GLFW_RELEASE ? 0 : 1);
				break;
			}
		}
	}

	public void setCursorMode(boolean locked)
	{
		if (this.locked == locked) return;

		this.locked = locked;
		GLFW.glfwSetInputMode(this.window.handle(), GLFW.GLFW_CURSOR, this.locked ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}

	public boolean getCursorMode()
	{
		return this.locked;
	}
}
