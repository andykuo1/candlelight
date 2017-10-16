package net.jimboi.boron.base.tick;

import org.bstone.tick.TickCounter;

/**
 * Created by Andy on 3/1/17.
 */
public class OldTickEngine implements Runnable
{
	private volatile boolean running = false;

	private boolean dirty = true;

	private final boolean limitFrameRate;
	private final double timeStep;//nanoseconds per tick

	private double timePrevious;
	private double timeLatency;

	private double timeCounter;
	private TickCounter updateCounter;
	private TickCounter frameCounter;

	private final OldTickHandler handler;

	public OldTickEngine(int ticksPerSecond, boolean limitFrameRate, OldTickHandler handler)
	{
		this.timeStep = 1000000000D / ticksPerSecond;
		this.limitFrameRate = limitFrameRate;
		this.handler = handler;

		this.updateCounter = new TickCounter();
		this.frameCounter = new TickCounter();
	}

	public OldTickEngine setUpdateCounter(TickCounter tickCounter)
	{
		this.updateCounter = tickCounter;
		return this;
	}

	public OldTickEngine setFrameCounter(TickCounter tickCounter)
	{
		this.frameCounter = tickCounter;
		return this;
	}

	@Override
	public void run()
	{
		this.timeCounter = System.currentTimeMillis();
		this.updateCounter.reset();
		this.frameCounter.reset();

		this.timePrevious = System.nanoTime();
		this.timeLatency = 0;

		this.running = true;
		this.handler.onFirstUpdate(this);
		while(this.running)
		{
			this.update();
		}
		this.handler.onLastUpdate(this);
		this.running = false;
	}

	protected void update()
	{
		final double current = System.nanoTime();
		final double elapsed = current - this.timePrevious;

		this.timePrevious = current;
		this.timeLatency += elapsed;

		this.handler.onPreUpdate();

		while(this.timeLatency >= this.timeStep)
		{
			this.handler.onFixedUpdate();
			this.timeLatency -= this.timeStep;
			this.updateCounter.tick();
			this.dirty = true;
		}

		if (this.dirty || !this.limitFrameRate)
		{
			this.handler.onUpdate(this.timeLatency / this.timeStep);
			this.frameCounter.tick();
			this.dirty = false;
		}

		if (System.currentTimeMillis() - this.timeCounter > 1000)
		{
			this.timeCounter += 1000;

			System.out.println("[UPS: " + this.updateCounter.get() + " || FPS: " + this.frameCounter.get() + "]");
		}
	}

	public void stop()
	{
		this.running = false;
	}

	public boolean isRunning()
	{
		return this.running;
	}
}