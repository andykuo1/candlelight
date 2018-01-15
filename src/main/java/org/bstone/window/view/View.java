package org.bstone.window.view;

import org.bstone.util.listener.Listenable;
import org.bstone.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Stack;

/**
 * Created by Andy on 1/15/18.
 */
public class View
{
	public final Listenable onViewPortChanged = new Listenable(this);

	private final Stack<ViewPort> viewports = new Stack<>();
	private final ViewPort defaultViewPort;

	private final Window window;

	public View(Window window)
	{
		this.window = window;
		this.defaultViewPort = new DefaultViewPort(this.window);
		this.setCurrentViewPort(this.defaultViewPort);
	}

	public ViewPort getCurrentViewPort()
	{
		return this.viewports.peek();
	}

	public void setCurrentViewPort(ViewPort viewport)
	{
		ViewPort prev = !this.viewports.empty() ? this.viewports.peek() : null;
		this.viewports.push(viewport);

		this.updateViewPort(viewport);

		this.onViewPortChanged.notifyListeners(new Object[]{
				viewport, prev
		});
	}

	public ViewPort removeCurrentViewPort()
	{
		if (this.viewports.size() <= 1)
		{
			throw new IllegalStateException("Window must have at least 1 viewport!");
		}

		ViewPort prev = this.viewports.pop();
		ViewPort viewport = this.viewports.peek();

		this.updateViewPort(viewport);

		this.onViewPortChanged.notifyListeners(new Object[]{
				viewport, prev
		});

		return prev;
	}

	public ViewPort removeViewPort(ViewPort viewport)
	{
		if (this.viewports.peek() == viewport)
		{
			return this.removeCurrentViewPort();
		}
		else
		{
			return this.viewports.remove(viewport) ? viewport : null;
		}
	}

	public void resetViewPort()
	{
		this.viewports.clear();
		this.setCurrentViewPort(this.defaultViewPort);
	}

	private void updateViewPort(ViewPort viewport)
	{
		if (viewport == this.defaultViewPort)
		{
			//FIXME: This is a fix (hack?) for Mac's weird hack to scale windows for compatibility
			try (MemoryStack stack = MemoryStack.stackPush())
			{
				IntBuffer pWidth = stack.mallocInt(1);
				IntBuffer pHeight = stack.mallocInt(1);

				GLFW.glfwGetFramebufferSize(this.window.handle(), pWidth, pHeight);
				GL11.glViewport(0, 0, pWidth.get(0), pHeight.get(0));
			}
		}
		else
		{
			GL11.glViewport(viewport.getX(), viewport.getY(),
					viewport.getWidth(), viewport.getHeight());
		}
	}
}
