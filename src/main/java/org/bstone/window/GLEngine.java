package org.bstone.window;

import org.bstone.application.kernel.Engine;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

/**
 * Created by Andy on 1/15/18.
 */
public class GLEngine implements Engine
{
	@Override
	public boolean initialize()
	{
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

		return true;
	}

	@Override
	public void update()
	{
	}

	@Override
	public void terminate()
	{
		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
}
