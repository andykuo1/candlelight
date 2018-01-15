package org.bstone.render;

import org.bstone.kernel.Engine;
import org.bstone.service.ServiceManager;
import org.bstone.window.Window;

/**
 * An engine that handles the rendering of the application
 */
public class RenderEngine implements Engine
{
	private final Window window;

	private final ServiceManager<RenderEngine, RenderService> services;

	private final boolean limitFrameRate;
	private final double timeStep;
	private double timeDelta;
	private double timePrevious;
	private boolean dirty = true;

	public RenderEngine(Window window, int framesPerSecond, boolean limitFrameRate)
	{
		this.window = window;

		this.services = new ServiceManager<>(this);

		this.limitFrameRate = limitFrameRate;
		this.timeStep = 1000000000D / framesPerSecond;
	}

	@Override
	public boolean initialize()
	{
		this.timePrevious = System.nanoTime();

		this.services.beginServices();
		this.services.endServices();

		return true;
	}

	@Override
	public void update()
	{
		this.services.beginServices();

		if (this.dirty || !this.limitFrameRate)
		{
			final double current = System.nanoTime();
			final double elapsed = current - this.timePrevious;
			this.timePrevious = current;
			this.timeDelta = elapsed / this.timeStep;

			this.window.clearScreenBuffer();
			{
				this.services.forEach(renderService -> renderService.onRenderUpdate(this, this.timeDelta));
				--this.timeDelta;
			}
			this.window.updateScreenBuffer();

			this.dirty = false;
		}

		this.window.poll();

		this.services.endServices();
	}

	@Override
	public void terminate()
	{
		this.services.beginServices();
		this.services.endServices();

		this.services.destroy();
	}

	public final Window getWindow()
	{
		return this.window;
	}

	public final ServiceManager<RenderEngine, RenderService> getRenderServices()
	{
		return this.services;
	}

	public double getElapsedFrameTime()
	{
		return this.timeDelta;
	}

	public void markDirty()
	{
		this.dirty = true;
	}
}
