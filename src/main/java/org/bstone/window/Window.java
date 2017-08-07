package org.bstone.window;

import org.bstone.util.listener.Listenable;
import org.bstone.window.input.InputEngine;
import org.bstone.window.view.ViewPort;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.Stack;

/**
 * Created by Andy on 4/6/17.
 */
public class Window
{
	public interface OnWindowSizeChanged
	{
		void onWindowSizeChanged(int width, int height, int prevWidth, int prevHeight);
	}

	public interface OnViewPortChanged
	{
		void onViewPortChanged(ViewPort viewport, ViewPort prevViewPort);
	}

	public final Listenable<OnWindowSizeChanged> onWindowSizeChanged = new Listenable<>(((listener, objects) -> listener.onWindowSizeChanged((Integer) objects[0], (Integer) objects[1], (Integer) objects[2], (Integer) objects[3])));

	public final Listenable<OnViewPortChanged> onViewPortChanged = new Listenable<>(((listener, objects) -> listener.onViewPortChanged((ViewPort) objects[0], (ViewPort) objects[1])));

	private final InputEngine inputEngine;

	private final Stack<ViewPort> viewports = new Stack<>();
	private final ViewPort defaultViewPort = new ViewPort(0, 0, this.width, this.height){
		@Override
		public void setWidth(int width)
		{
			throw new UnsupportedOperationException("Cannot change default viewport size!");
		}

		@Override
		public void setHeight(int height)
		{
			throw new UnsupportedOperationException("Cannot change default viewport size!");
		}

		@Override
		public int getX()
		{
			return 0;
		}

		@Override
		public int getY()
		{
			return 0;
		}

		@Override
		public int getWidth()
		{
			return Window.this.width;
		}

		@Override
		public int getHeight()
		{
			return Window.this.height;
		}
	};

	private int width;
	private int height;

	private long handle;

	public Window(String title, int width, int height)
	{
		this.width = width;
		this.height = height;

		System.out.println("LWJGL " + Version.getVersion());
		System.out.println("JOML 1.9.2");

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !GLFW.glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable

		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		// Create the window
		this.handle = GLFW.glfwCreateWindow(this.width, this.height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if ( handle == MemoryUtil.NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		//Create the input context
		this.inputEngine = new InputEngine(this);

		//Setup a size callback.
		GLFW.glfwSetWindowSizeCallback(this.handle, (window, w, h) ->
		{
			int prevW = this.width;
			int prevH = this.height;
			this.width = w;
			this.height = h;
			this.onWindowSizeChanged.notifyListeners(this.width, this.height, prevW, prevH);
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			GLFW.glfwGetWindowSize(handle, pWidth, pHeight);

			this.width = pWidth.get(0);
			this.height = pHeight.get(0);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(
					handle,
					(vidmode.width() - this.width) / 2,
					(vidmode.height() - this.height) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(handle);
		// Enable v-sync
		GLFW.glfwSwapInterval(1);

		// Make the window visible
		GLFW.glfwShowWindow(handle);

		//Focus the window
		GLFW.glfwFocusWindow(handle);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		//Enable depth testing
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		//Set current viewport
		this.setCurrentViewPort(this.defaultViewPort);
	}

	public void clearScreenBuffer()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void updateScreenBuffer()
	{
		GLFW.glfwSwapBuffers(this.handle);
	}

	public void updateInput()
	{
		this.inputEngine.update();
	}

	public void poll()
	{
		GLFW.glfwPollEvents();
	}

	public void destroy()
	{
		// Free the window callbacks and destroy the window
		Callbacks.glfwFreeCallbacks(handle);
		GLFW.glfwDestroyWindow(handle);

		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	public void setWindowTitle(String title)
	{
		GLFW.glfwSetWindowTitle(this.handle, title);
	}

	public void setWindowSize(int width, int height)
	{
		GLFW.glfwSetWindowSize(this.handle, width, height);
	}

	public boolean shouldCloseWindow()
	{
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public InputEngine getInputEngine()
	{
		return this.inputEngine;
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

		this.onViewPortChanged.notifyListeners(viewport, prev);
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

		this.onViewPortChanged.notifyListeners(viewport, prev);

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
			//This is a fix (hack?) for Mac's weird hack to scale windows for compatibility
			try (MemoryStack stack = MemoryStack.stackPush())
			{
				IntBuffer pWidth = stack.mallocInt(1);
				IntBuffer pHeight = stack.mallocInt(1);

				GLFW.glfwGetFramebufferSize(this.handle, pWidth, pHeight);
				GL11.glViewport(0, 0, pWidth.get(0), pHeight.get(0));
			}
		}
		else
		{
			GL11.glViewport(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
		}
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public long handle()
	{
		return this.handle;
	}
}
