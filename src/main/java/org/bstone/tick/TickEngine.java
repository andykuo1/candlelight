package org.bstone.tick;

import org.bstone.application.Application;
import org.bstone.application.Engine;
import org.bstone.render.FrameHandler;
import org.bstone.service.ServiceManager;

/**
 * An engine that handles fixed updating of the application
 */
public class TickEngine extends Engine implements FrameHandler
{
	private boolean dirty = true;

	private final boolean limitFrameRate;
	private final double timeStep;//nanoseconds per tick

	private double timePrevious;
	private double timeLatency;

	private double timeDelta;

	private final ServiceManager<TickEngine, TickService> services;

	private final TickCounter updateCounter;

	public TickEngine(int ticksPerSecond, boolean limitFrameRate)
	{
		this.timeStep = 1000000000D / ticksPerSecond;
		this.limitFrameRate = limitFrameRate;

		this.services = new ServiceManager<>(this);

		this.updateCounter = new TickCounter();
	}

	@Override
	protected boolean onStart(Application app)
	{
		this.updateCounter.reset();

		this.timePrevious = System.nanoTime();
		this.timeLatency = 0;

		this.services.beginServices();
		this.services.endServices();

		return true;
	}

	@Override
	protected void onUpdate(Application app)
	{
		final double current = System.nanoTime();
		final double elapsed = current - this.timePrevious;

		this.timePrevious = current;
		this.timeLatency += elapsed;

		this.services.beginServices();
		this.services.forEach(TickService::onEarlyUpdate);

		while(this.timeLatency >= this.timeStep)
		{
			this.services.forEach(TickService::onFixedUpdate);

			this.timeLatency -= this.timeStep;
			this.updateCounter.tick();
			this.dirty = true;
		}

		this.services.forEach(TickService::onLateUpdate);
		this.services.endServices();

		this.timeDelta = this.timeLatency / this.timeStep;
	}

	@Override
	protected void onStop(Application app)
	{
		this.services.beginServices();
		this.services.endServices();

		this.services.destroy();
	}

	public final TickCounter getUpdateCounter()
	{
		return this.updateCounter;
	}

	@Override
	public boolean shouldRenderCurrentFrame()
	{
		return this.dirty || !this.limitFrameRate;
	}

	@Override
	public double getElapsedFrameTime()
	{
		return this.timeDelta;
	}

	@Override
	public void onFrameRendered()
	{
		this.dirty = false;
	}

	public final ServiceManager<TickEngine, TickService> getTickServices()
	{
		return this.services;
	}
}
