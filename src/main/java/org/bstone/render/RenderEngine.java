package org.bstone.render;

import org.bstone.service.ServiceManager;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

/**
 * Created by Andy on 8/4/17.
 */
public class RenderEngine
{
	private final ServiceManager<RenderService, RenderEngine> serviceManager;

	private final RenderHandler handler;

	public RenderEngine(RenderHandler handler)
	{
		this.handler = handler;

		this.serviceManager = new ServiceManager<>(this);

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

	public void destroy()
	{
		// Terminate GLFW and free the error callback
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	public void load()
	{
		this.handler.onRenderLoad(this);
	}

	public void unload()
	{
		this.handler.onRenderUnload(this);
		this.serviceManager.clearServices();
	}

	public void update(double delta)
	{
		this.serviceManager.beginServiceBlock();
		{
			this.handler.onRenderUpdate(this, delta);
			this.serviceManager.forEach(service->service.onRenderUpdate(this, delta));
		}
		this.serviceManager.endServiceBlock();
	}

	public final ServiceManager<RenderService, org.bstone.render.RenderEngine> getRenderServices()
	{
		return this.serviceManager;
	}
}
