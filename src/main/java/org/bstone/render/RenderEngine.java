package org.bstone.render;

import org.bstone.kernel.Engine;
import org.bstone.scheduler.Scheduler;
import org.bstone.service.ServiceManager;
import org.bstone.tick.TickCounter;
import org.bstone.window.Window;

/**
 * An engine that handles the rendering of the application
 */
public class RenderEngine implements Engine
{
	private final Window window;

	private final ServiceManager<RenderEngine, RenderService> services;
	private final Scheduler scheduler;

	private final TickCounter frameCounter;

	private final boolean limitFrameRate;
	private double prevFrameTime;
	private boolean dirty = true;

	public RenderEngine(Window window, boolean limitFrameRate)
	{
		this.window = window;

		this.services = new ServiceManager<>(this);
		this.scheduler = new Scheduler();

		this.frameCounter = new TickCounter();

		this.limitFrameRate = limitFrameRate;
	}

	@Override
	public boolean initialize()
	{
		this.frameCounter.reset();

		this.services.beginServices();
		this.scheduler.process();
		this.services.endServices();

		return true;
	}

	@Override
	public void update()
	{
		this.services.beginServices();

		if (this.dirty || !this.limitFrameRate)
		{
			this.window.clearScreenBuffer();
			{
				final double frameTime = System.nanoTime();
				final double delta = frameTime - this.prevFrameTime;
				this.prevFrameTime = frameTime;

				this.services.forEach(renderService -> renderService.onRenderUpdate(this, delta));
			}
			this.window.updateScreenBuffer();

			this.dirty = false;
			this.frameCounter.tick();
		}

		this.window.poll();

		this.scheduler.process();
		this.services.endServices();
	}

	@Override
	public void terminate()
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

	public void markDirty()
	{
		this.dirty = true;
	}

	public final Scheduler getScheduler()
	{
		return this.scheduler;
	}
}
