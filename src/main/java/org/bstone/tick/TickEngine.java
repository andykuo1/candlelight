package org.bstone.tick;

import org.bstone.kernel.Engine;

/**
 * An engine that handles fixed updating of the application
 */
public class TickEngine implements Engine
{
	private final double timeStep;//nanoseconds per tick

	private double timePrevious;
	private double timeLatency;

	private double timeDelta;

	private Tickable tickable;

	public TickEngine(int ticksPerSecond)
	{
		this.timeStep = 1000000000D / ticksPerSecond;
	}

	public TickEngine setTickable(Tickable tickable)
	{
		this.tickable = tickable;
		return this;
	}

	@Override
	public boolean initialize()
	{
		if (this.tickable == null)
			throw new IllegalStateException("missing tickable");

		this.timePrevious = System.nanoTime();
		this.timeLatency = 0;

		return true;
	}

	@Override
	public void update()
	{
		final double current = System.nanoTime();
		final double elapsed = current - this.timePrevious;

		this.timePrevious = current;
		this.timeLatency += elapsed;

		while(this.timeLatency >= this.timeStep)
		{
			this.tickable.tick();
			this.timeLatency -= this.timeStep;
		}

		this.timeDelta = this.timeLatency / this.timeStep;
	}

	@Override
	public void terminate()
	{
	}

	public double getElapsedTickTime()
	{
		return this.timeDelta;
	}

	public final Tickable getTickable()
	{
		return this.tickable;
	}
}
