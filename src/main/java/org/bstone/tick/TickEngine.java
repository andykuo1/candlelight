package org.bstone.tick;

import org.bstone.kernel.Engine;
import org.bstone.scheduler.Scheduler;
import org.bstone.service.ServiceManager;

/**
 * An engine that handles fixed updating of the application
 */
public class TickEngine implements Engine
{
	private final double timeStep;//nanoseconds per tick

	private double timePrevious;
	private double timeLatency;

	private double timeDelta;

	private final ServiceManager<TickEngine, TickService> services;
	private final Scheduler scheduler;

	private final TickCounter updateCounter;

	public TickEngine(int ticksPerSecond)
	{
		this.timeStep = 1000000000D / ticksPerSecond;

		this.services = new ServiceManager<>(this);
		this.scheduler = new Scheduler();

		this.updateCounter = new TickCounter();
	}

	@Override
	public boolean initialize()
	{
		this.updateCounter.reset();

		this.timePrevious = System.nanoTime();
		this.timeLatency = 0;

		this.services.beginServices();
		this.scheduler.process();
		this.services.endServices();

		return true;
	}

	@Override
	public void update()
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
		}

		this.services.forEach(TickService::onLateUpdate);
		this.scheduler.process();
		this.services.endServices();

		this.timeDelta = this.timeLatency / this.timeStep;
	}

	@Override
	public void terminate()
	{
		this.services.beginServices();
		this.scheduler.process();
		this.services.endServices();

		this.services.destroy();
	}

	public final TickCounter getUpdateCounter()
	{
		return this.updateCounter;
	}

	public double getElapsedTickTime()
	{
		return this.timeDelta;
	}

	public final ServiceManager<TickEngine, TickService> getTickServices()
	{
		return this.services;
	}
}
