package org.bstone.tick;

import org.bstone.application.Application;
import org.bstone.application.Engine;

/**
 * Created by Andy on 10/12/17.
 */
public class TickEngine extends Engine
{
	private boolean dirty = true;

	private final boolean limitFrameRate;
	private final double timeStep;//nanoseconds per tick

	private double timePrevious;
	private double timeLatency;

	private TickCounter updateCounter;

	private double timeDelta;

	private final TickHandler handler;

	public TickEngine(int ticksPerSecond, boolean limitFrameRate, TickHandler handler)
	{
		this.timeStep = 1000000000D / ticksPerSecond;
		this.limitFrameRate = limitFrameRate;
		this.handler = handler;

		this.updateCounter = new TickCounter();
	}

	@Override
	protected boolean onStart(Application app)
	{
		this.updateCounter.reset();

		this.timePrevious = System.nanoTime();
		this.timeLatency = 0;

		this.handler.onFirstUpdate();
		return true;
	}

	@Override
	protected void onUpdate(Application app)
	{
		final double current = System.nanoTime();
		final double elapsed = current - this.timePrevious;

		this.timePrevious = current;
		this.timeLatency += elapsed;

		this.handler.onEarlyUpdate();

		while(this.timeLatency >= this.timeStep)
		{
			this.handler.onFixedUpdate();
			this.timeLatency -= this.timeStep;
			this.updateCounter.tick();
			this.dirty = true;
		}

		this.handler.onLateUpdate();

		this.timeDelta = this.timeLatency / this.timeStep;
	}

	@Override
	protected void onStop(Application app)
	{
		this.handler.onLastUpdate();
	}

	public TickCounter getUpdateCounter()
	{
		return this.updateCounter;
	}

	public void setFrameUpdated()
	{
		this.dirty = false;
	}

	public boolean shouldRenderFrame()
	{
		return this.dirty || !this.limitFrameRate;
	}

	public double getElapsedFrameTime()
	{
		return this.timeDelta;
	}
}
