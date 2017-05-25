package org.bstone.window;

import org.bstone.input.InputEngine;
import org.bstone.poma.Poma;
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
	private final InputEngine inputEngine;

	private int width;
	private int height;

	private boolean dirty = true;

	private long handle;

	public Window(String title, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.dirty = true;//TODO: this needs to be true

		Poma.OUT("LWJGL " + Version.getVersion() + "!");

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
		GLFW.glfwSetWindowSizeCallback(handle, (window, w, h) -> {
			this.width = w;
			this.height = h;
			this.dirty = true;
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
	}

	public void update()
	{
		if (this.dirty)
		{
			//TODO: Why is this x2?
			GL11.glViewport(0, 0, this.width * 2, this.height * 2);
			this.dirty = false;
		}

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void poll()
	{
		GLFW.glfwSwapBuffers(this.handle);
		GLFW.glfwPollEvents();

		this.inputEngine.update();
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

	public boolean shouldCloseWindow()
	{
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public InputEngine getInputEngine()
	{
		return this.inputEngine;
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
