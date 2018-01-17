package org.bstone.window;

import org.bstone.util.listener.Listenable;
import org.bstone.window.view.View;
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

/**
 * Created by Andy on 4/6/17.
 */
public class Window
{
	private static boolean initGLFW = false;
	public static void initializeGLFW()
	{
		if (initGLFW)
		{
			return;
		}
		else
		{
			initGLFW = true;
		}

		System.out.println("OS " + System.getProperty("os.name"));
		System.out.println("JAVA " + System.getProperty("java.version"));
		System.out.println("LWJGL " + Version.getVersion());
		System.out.println("GLFW " + GLFW.glfwGetVersionString());
		System.out.println("JOML 1.9.2");

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!GLFW.glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}
	}

	public static void terminateGLFW()
	{
		if (!initGLFW)
		{
			return;
		}
		else
		{
			initGLFW = false;
		}

		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	public static void setCurrentWindowContext(Window window)
	{
		GLFW.glfwMakeContextCurrent(window.handle);
	}

	public static GLFWVidMode getCurrentVideoMode()
	{
		return GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
	}

	public final Listenable onWindowSizeChanged = new Listenable(this);
	public final Listenable onWindowPosChanged = new Listenable(this);

	private String title = "Window";
	private int width = 640;
	private int height = 480;
	private int x;
	private int y;
	private boolean vsync = true;

	private View view;

	private long handle;

	public Window()
	{
		this.handle = MemoryUtil.NULL;
	}

	public Window setResizeable(boolean resizeable)
	{
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,
				resizeable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		return this;
	}

	public Window setPosition(int x, int y)
	{
		if (this.handle == MemoryUtil.NULL)
		{
			this.x = x;
			this.y = y;
		}
		else
		{
			GLFW.glfwSetWindowPos(this.handle, x, y);
		}
		return this;
	}

	public Window setSize(int width, int height)
	{
		if (this.handle == MemoryUtil.NULL)
		{
			this.width = width;
			this.height = height;
		}
		else
		{
			GLFW.glfwSetWindowSize(this.handle, width, height);
		}
		return this;
	}

	public Window setTitle(String title)
	{
		if (this.handle == MemoryUtil.NULL)
		{
			this.title = title;
		}
		else
		{
			GLFW.glfwSetWindowTitle(this.handle, this.title = title);
		}
		return this;
	}

	public Window setVSync(boolean vsync)
	{
		if (this.handle == MemoryUtil.NULL)
		{
			this.vsync = vsync;
		}
		else
		{
			GLFW.glfwSwapInterval((this.vsync = vsync) ? 1 : 0);
		}
		return this;
	}

	public void show()
	{
		if (this.handle == MemoryUtil.NULL)
		{
			this.createWindow();
		}

		GLFW.glfwShowWindow(this.handle);
		GLFW.glfwFocusWindow(this.handle);
	}

	public void hide()
	{
		if (this.handle == MemoryUtil.NULL)
			throw new IllegalStateException("window not yet initialized!");

		GLFW.glfwHideWindow(this.handle);
	}

	protected void createWindow()
	{
		// the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);

		// Configure GLFW
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE,
				GLFW.GLFW_OPENGL_CORE_PROFILE);

		// Create the window
		this.handle = GLFW.glfwCreateWindow(this.width, this.height, this.title,
				MemoryUtil.NULL, MemoryUtil.NULL);
		if (this.handle == MemoryUtil.NULL)
		{
			throw new RuntimeException("Failed to create the GLFW window");
		}

		//Create the input context
		//this.inputEngine = new InputEngine(this);

		//Setup a size callback.
		GLFW.glfwSetWindowSizeCallback(this.handle, (window, w, h) ->
		{
			int prevW = this.width;
			int prevH = this.height;
			this.width = w;
			this.height = h;
			this.onWindowSizeChanged.notifyListeners(new int[]{
					this.width, this.height, prevW, prevH
			});
		});

		//Setup a position callback
		GLFW.glfwSetWindowPosCallback(this.handle, (window, xpos, ypos) -> {
			int prevX = this.x;
			int prevY = this.y;
			this.x = xpos;
			this.y = ypos;
			this.onWindowPosChanged.notifyListeners(new int[]{
					this.x, this.y, prevX, prevY
			});
		});

		this.makeWindowCentered();

		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(this.handle);

		// Enable v-sync
		GLFW.glfwSwapInterval(this.vsync ? 1 : 0);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		System.out.println("OPENGL " + GL11.glGetString(GL11.GL_VERSION));

		// Set the clear color
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		//Enable depth testing
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		//Set current viewport
		this.view = new View(this);
	}

	public void makeWindowCentered()
	{
		// Get the thread stack and push a new frame
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			GLFW.glfwGetWindowSize(this.handle, pWidth, pHeight);

			this.width = pWidth.get(0);
			this.height = pHeight.get(0);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = getCurrentVideoMode();

			// Center the window
			GLFW.glfwSetWindowPos(
					this.handle,
					this.x = ((vidmode.width() - this.width) / 2),
					this.y = ((vidmode.height() - this.height) / 2)
			);
		} // the stack frame is popped automatically
	}

	public void clearScreenBuffer()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void updateScreenBuffer()
	{
		GLFW.glfwSwapBuffers(this.handle);
	}

	public void poll()
	{
		GLFW.glfwPollEvents();
	}

	public void destroy()
	{
		// Free the window callbacks and destroy the window
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle);
	}

	public boolean shouldWindowClose()
	{
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public int getRefreshRate()
	{
		return getCurrentVideoMode().refreshRate();
	}

	public View getView()
	{
		return this.view;
	}

	public String getTitle()
	{
		return this.title;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
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
