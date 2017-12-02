package org.bstone.render;

import org.bstone.application.Application;
import org.bstone.application.Engine;
import org.bstone.scheduler.Scheduler;
import org.bstone.service.ServiceManager;
import org.bstone.tick.TickCounter;
import org.bstone.window.Window;

/**
 * An engine that handles the rendering of the application
 */
public class RenderEngine extends Engine
{
	private final Window window;

	private final ServiceManager<RenderEngine, RenderService> services;
	private final Scheduler scheduler;

	private final TickCounter frameCounter;
	private final FrameHandler frameHandler;

	public RenderEngine(Window window, FrameHandler frameHandler)
	{
		this.window = window;

		this.services = new ServiceManager<>(this);
		this.scheduler = new Scheduler();

		this.frameHandler = frameHandler;

		this.frameCounter = new TickCounter();
	}

	@Override
	protected boolean onStart(Application app)
	{
		this.frameCounter.reset();

		this.services.beginServices();
		this.scheduler.process();
		this.services.endServices();

		return true;
	}

	@Override
	protected void onUpdate(Application app)
	{
		this.services.beginServices();

		if (this.frameHandler.shouldRenderCurrentFrame())
		{
			this.window.clearScreenBuffer();
			{
				double delta = this.frameHandler.getElapsedFrameTime();

				this.services.forEach(renderService -> renderService.onRenderUpdate(this, delta));
			}
			this.window.updateScreenBuffer();

			this.frameHandler.onFrameRendered();

			this.frameCounter.tick();
		}

		this.window.poll();

		if (this.window.shouldCloseWindow())
		{
			app.stop();
		}

		this.scheduler.process();
		this.services.endServices();
	}

	@Override
	protected void onStop(Application app)
	{
		this.services.beginServices();
		this.scheduler.process();
		this.services.endServices();

		this.services.destroy();
	}

	public final Window getWindow()
	{
		return this.window;
	}

	public final TickCounter getFrameCounter()
	{
		return this.frameCounter;
	}

	public final ServiceManager<RenderEngine, RenderService> getRenderServices()
	{
		return this.services;
	}

	public final Scheduler getScheduler()
	{
		return this.scheduler;
	}
}
